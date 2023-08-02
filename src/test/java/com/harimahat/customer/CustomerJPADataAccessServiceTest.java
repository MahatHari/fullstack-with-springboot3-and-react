package com.harimahat.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * @Author hari.mahat on 1.8.2023
 * Project learn-spring3
 */
class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable= MockitoAnnotations.openMocks(this);
        underTest= new CustomerJPADataAccessService(customerRepository);
    }
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        // WHEN
            underTest.selectAllCustomers();
        // THEN
        verify(customerRepository)
                .findAll();
    }

    @Test
    void selectCustomerById() {
        // GIVEN
        Long Id=2L;
        // WHEN
        underTest.selectCustomerById(Id);
        // THEN
        verify(customerRepository).findById(Id);
    }

    @Test
    void insertCustomer() {
        // GIVEN
        Customer customer= new Customer(2L, "Hari", "example.com", 24);
        // WHEN
        underTest.insertCustomer(customer);
        // THEN
        verify(customerRepository).save(customer);
    }

    @Test
    void deleteCustomerById() {
        // GIVEN
        Long Id=2L;
        // WHEN
        underTest.deleteCustomerById(Id);
        // THEN
        verify(customerRepository).deleteById(Id);
    }

    @Test
    void updateCustomer( ) {
        /// GIVEN
        Customer customer= new Customer(2L, "Hari", "example.com", 24);
        // WHEN
        underTest.updateCustomer(customer);
        // THEN
        verify(customerRepository).save(customer);
    }

    @Test
    void existPersonWithEmail() {
        // GIVEN
        String email="example@example.com";
        // WHEN
        underTest.existPersonWithEmail(email);
        // THEN
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existPersonWithId() {
        // GIVEN
        Long id=2L;
        // WHEN
        underTest.existPersonWithId(id);
        // THEN
        verify(customerRepository).existsCustomerById(id);
    }
}