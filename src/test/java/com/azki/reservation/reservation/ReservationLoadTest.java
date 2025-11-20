package com.azki.reservation.reservation;

import com.azki.reservation.BaseE2ETest;
import com.azki.reservation.api.v1.model.ApiResponse;
import com.azki.reservation.api.v1.response.ReserveResponse;
import com.azki.reservation.domain.Customer;
import com.azki.reservation.domain.Slot;
import com.azki.reservation.repository.CustomerRepository;
import com.azki.reservation.repository.SlotRepository;
import com.azki.reservation.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

// NOTE:
// The latency and success metrics observed in this test are not an exact reflection of real server performance.
// Since this load test runs locally on a laptop and uses TestRestTemplate, client‑side I/O, thread scheduling,
// and limited CPU resources can dominate timing. As a result, there’s often little measurable difference between
// 10 and 100 concurrent users under these conditions.
// You can experiment by changing the Bulkhead configuration (max‑concurrent‑calls) to confirm correct behavior.
// The stable average response time and low variance across different loads indicate that, from a logic and
// concurrency control perspective, the reservation service is handling spikes and concurrent traffic effectively.

@Slf4j
class ReservationLoadTest extends BaseE2ETest {

    private static final int CONCURRENT_USERS = 130;
    private static final String RESERVATION_ENDPOINT = "/api/reservations";

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private Environment env;

    private List<Customer> mockCustomers;

    @BeforeEach
    void setupData() {
        mockCustomers = IntStream.range(0, CONCURRENT_USERS)
                .mapToObj(i -> new Customer().setMail("loadtest_" + i + "@azki.ir"))
                .map(customerRepository::save)
                .toList();

        Date now = new Date();
        List<Slot> slots = IntStream.range(0, 150)
                .mapToObj(i -> new Slot()
                        .setStartTime(new Date(now.getTime() + i * 60_000))
                        .setEndTime(new Date(now.getTime() + (i + 1) * 60_000))
                        .setReserved(false))
                .toList();

        slotRepository.saveAll(slots);
    }

    @Test
    void shouldHandleConcurrentReservationsSafely() throws InterruptedException {
        int maxConcurrentCalls = env.getProperty(
                "resilience4j.bulkhead.instances.reserveBulkhead.max-concurrent-calls",
                Integer.class,
                100
        );

        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_USERS);
        CountDownLatch latch = new CountDownLatch(CONCURRENT_USERS);

        List<Future<RequestResult>> futures = IntStream.range(0, CONCURRENT_USERS)
                .mapToObj(i -> executor.submit(() -> {
                    Customer customer = mockCustomers.get(i);
                    long start = System.nanoTime();
                    try {
                        HttpHeaders headers = new HttpHeaders();
                        String token = jwtUtil.generateCustomerToken(customer.getId(), customer.getMail(), "ROLE_CUSTOMER");
                        headers.set("Authorization", token);
                        headers.setContentType(MediaType.APPLICATION_JSON);

                        ResponseEntity<ApiResponse<ReserveResponse>> res = rest.exchange(
                                RESERVATION_ENDPOINT,
                                HttpMethod.POST,
                                new HttpEntity<>(null, headers),
                                new ParameterizedTypeReference<>() {
                                }
                        );

                        long durationMs = (System.nanoTime() - start) / 1_000_000;
                        boolean success = res.getStatusCode() == HttpStatus.OK &&
                                Objects.equals(res.getBody().getStatus(), "SUCCESS");
                        log.info("User {} - status: {}, latency: {} ms",
                                customer.getMail(),
                                success ? "SUCCESS" : "FAIL",
                                durationMs);
                        return new RequestResult(success, durationMs);
                    } catch (Exception e) {
                        long durationMs = (System.nanoTime() - start) / 1_000_000;
                        log.info("User {} - status: EXCEPTION, latency: {} ms",
                                customer.getMail(), durationMs);
                        return new RequestResult(false, durationMs);
                    } finally {
                        latch.countDown();
                    }
                }))
                .toList();

        latch.await();
        executor.shutdown();

        List<RequestResult> results = futures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        return new RequestResult(false, 0);
                    }
                })
                .toList();

        long successCount = results.stream().filter(r -> r.success).count();
        long failedCount = CONCURRENT_USERS - successCount;

        double avgLatency = results.stream().mapToLong(r -> r.durationMs).average().orElse(0);
        long maxLatency = results.stream().mapToLong(r -> r.durationMs).max().orElse(0);

        log.info("Successful: {}", successCount);
        log.info("Failed: {}", failedCount);
        log.info("Average latency: {} ms, Max latency: {} ms", avgLatency, maxLatency);

        Assertions.assertTrue(successCount >= maxConcurrentCalls,
                "Expected at least " + maxConcurrentCalls + " successful reservations but got " + successCount);
    }

    private record RequestResult(boolean success, long durationMs) {
    }
}
