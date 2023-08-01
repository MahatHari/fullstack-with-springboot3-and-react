package com.harimahat.customer;

import com.harimahat.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author hari.mahat on 1.8.2023
 * Project learn-spring3
 */
class CustomerJDBCDataAccessServiceTest extends AbstractTestContainer {
    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper= new CustomerRowMapper();


    @BeforeEach
    void setUp() {
        underTest= new CustomerJDBCDataAccessService(
                getJdbcTemplate(), customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        // GIVEN
            Customer customer= new Customer(
                Faker.name().fullName(),
                    Faker.internet().emailAddress()+"-"+ UUID.randomUUID(),
                    20
            );
            underTest.insertCustomer(customer);
        // WHEN
           List<Customer> actual= underTest.selectAllCustomers();

        // THEN
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {

        // GIVEN
        String email=Faker.internet().emailAddress()+"-"+ UUID.randomUUID();
        Customer customer= new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
       Long id= underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // WHEN
        Optional<Customer> actual= underTest.selectCustomerById(id);

        // THEN
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getAge()).isEqualTo(20);
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        // GIVEN
        Long id= -0L;

        // WHEN 
    var actual= underTest.selectCustomerById(id);
        // THEN

        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        String email=Faker.internet().emailAddress()+"-"+ UUID.randomUUID();
        Customer customer= new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        Long id= underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        customer.setId(id);
        // THEN
        Optional<Customer> actual= underTest.selectCustomerById(id);
        assertThat(actual).isPresent();
       assertThat(actual).isPresent().hasValueSatisfying(act->{
           assertThat(act.getId()).isEqualTo(customer.getId());
           assertThat(act.getName()).isEqualTo(customer.getName());
           assertThat(act.getAge()).isEqualTo(customer.getAge());
           assertThat(act.getEmail()).isEqualTo(customer.getEmail());
       });

    }

    @Test
    void deleteCustomerById() {

        //Given
        String email=Faker.internet().emailAddress()+"-"+ UUID.randomUUID();
        Customer customer= new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        Long id= underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //WHEN
        underTest.deleteCustomerById(id);

        // THEN
        Optional<Customer> actual= underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomerName() {
        //Given
        String email=Faker.internet().emailAddress()+"-"+ UUID.randomUUID();
        Customer customer= new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        Long id= underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //WHEN
        var newName="foo";
        Customer update= new Customer();
        update.setId(id);
        update.setName(newName);
        underTest.updateCustomer(update);
        // THEN
        Optional<Customer> actual= underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id); // this is becoz we are not setting id for customer in Fake customer
            assertThat(c.getName()).isEqualTo(newName); //change
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
    @Test
    void updateCustomerEmail() {
        //Given
        String email=Faker.internet().emailAddress()+"-"+ UUID.randomUUID();
        Customer customer= new Customer(
                Faker.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        Long id= underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        //WHEN
        var newEmail="hkm@gmail.com";
        Customer update= new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateCustomer(update);
        // THEN
        Optional<Customer> actual= underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id); // this is becoz we are not setting id for customer in Fake customer
            assertThat(c.getName()).isEqualTo(customer.getName()); //change
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
    @Test
    void updateCustomerAge() {
        //Given
        String email=Faker.internet().emailAddress()+"-"+ UUID.randomUUID();
        Customer customer= new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        Long id= underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //WHEN
        var newAge=30;
        Customer update= new Customer();
        update.setId(id);
        update.setAge(newAge);
        underTest.updateCustomer(update);
        // THEN
        Optional<Customer> actual= underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id); // this is becoz we are not setting id for customer in Fake customer
            assertThat(c.getName()).isEqualTo(customer.getName()); //change
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void existPersonWithEmail() {
        // GIVEN
        String email=Faker.internet().emailAddress()+"-"+ UUID.randomUUID();
        Customer customer= new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        // WHEN
        boolean actual= underTest.existPersonWithEmail(email);
        // THEN
        assertThat(actual).isTrue();
    }
    @Test
    void existPersonWithEmailReturnsFalseWhenDoesNotExist() {
        // GIVEN
        String email=Faker.internet().emailAddress()+"-"+ UUID.randomUUID();
        // WHEN
        boolean actual= underTest.existPersonWithEmail(email);
        // THEN
        assertThat(actual).isFalse();
    }

    @Test
    void existPersonWithId() {
        // GIVEN
        String email=Faker.internet().emailAddress()+"-"+ UUID.randomUUID();
        Customer customer= new Customer(
                Faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        Long id= underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // WHEN
        boolean actual= underTest.existPersonWithId(id);
        // THEN
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithIdReturnsFalseWhenDoesNotExists() {
        // GIVEN
            Long id=-1L;
        // WHEN 
        boolean actual= underTest.existPersonWithId(id);
        // THEN
        assertThat(actual).isFalse();
    }
}