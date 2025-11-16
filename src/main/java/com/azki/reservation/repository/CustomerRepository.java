package com.azki.reservation.repository;

import com.azki.reservation.domain.Customer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Cacheable(
            value = "customer-mail",
            key = "#mail",
            unless = "#result == null"
    )
    Optional<Customer> findByMail(String mail);

    @Cacheable(
            value = "customer-mail-password",
            key = "#mail+#password",
            unless = "#result == null"
    )
    Optional<Customer> findByPasswordAndMail(String password, String mail);

    @Cacheable(
            value = "customer-id",
            key = "#id",
            unless = "#result == null"
    )
    @NonNull
    @Override
    Optional<Customer> findById(@NonNull Long id);
}
