package com.harimahat.customer;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author hari.mahat on 28.7.2023
 * Project learn-spring3
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

        boolean existsCustomerByEmail(String email);
        boolean existsCustomerById(Long id);
}
