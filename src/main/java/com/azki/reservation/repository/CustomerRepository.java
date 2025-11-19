package com.azki.reservation.repository;

import com.azki.reservation.domain.Customer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

//TODO CacheEvict or CachePut?
// I know I have to evict or put cache. I don't have any process now to need cache put
// but I aware that (this is just for the interview reviewer)
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Cacheable(
            value = "customer-mail",
            key = "#root.methodName+#mail",
            unless = "#result == null"
    )
    Optional<Customer> findByMail(String mail);

    @Cacheable(
            value = "customer-mail-password",
            key = "#root.methodName+#mail+#password",
            unless = "#result == null"
    )
    Optional<Customer> findByPasswordAndMail(String password, String mail);

    @Cacheable(
            value = "customer-id",
            key = "#root.methodName+#id",
            unless = "#result == null"
    )
    @NonNull
    @Override
    Optional<Customer> findById(@NonNull Long id);
}
