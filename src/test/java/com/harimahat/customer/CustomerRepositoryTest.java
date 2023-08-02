package com.harimahat.customer;

import com.harimahat.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;



/**
 * @Author hari.mahat on 1.8.2023
 * Project learn-spring3
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainer {

    @Autowired
    private CustomerRepository underTest;




    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        String email= Faker.internet().safeEmailAddress()+ UUID.randomUUID();
        Customer customer= new Customer(
                Faker.name().fullName(),
                email,
                30
        );

        underTest.save(customer);

        // WHEN
        var actual= underTest.existsCustomerByEmail(email);
        // THEN

        assertThat(actual).isTrue();
    }
    @Test
    void existsCustomerByEmailReturnFlaseWhenEmailDoesNotExists() {
        String email= Faker.internet().safeEmailAddress()+ UUID.randomUUID();

        // WHEN
        var actual= underTest.existsCustomerByEmail(email);
        // THEN

        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        // GIVEN
        String email= Faker.internet().safeEmailAddress()+ UUID.randomUUID();
        Customer customer= new Customer(
                Faker.name().fullName(),
                email,
                30
        );

        underTest.save(customer);

        Long id= underTest.findAll()
                .stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // WHEN
        var actual= underTest.existsCustomerById(id);
        // THEN

        assertThat(actual).isTrue();
    }
    @Test
    void existsCustomerByIdReturnsFalseWhenIdDoesNotExists() {
        // GIVEN
        Long Id=-1L;
        // THEN
    var actual=underTest.existsCustomerById(Id);
        assertThat(actual).isFalse();
    }
}