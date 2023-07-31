package com.harimahat.customer;

import java.util.List;
import java.util.Optional;

/**
 * @Author hari.mahat on 27.7.2023
 * Project learn-spring3
 */

public interface CustomerDao {

    List<Customer> selectAllCustomers();

    Optional <Customer> selectCustomerById(Long customerId);

    void insertCustomer( Customer customer);

    void  deleteCustomerById(Long customerId);

    void  updateCustomer(Customer update);

    boolean existPersonWithEmail(String emailcustomer);

    boolean existPersonWithId(Long customerId);



}
