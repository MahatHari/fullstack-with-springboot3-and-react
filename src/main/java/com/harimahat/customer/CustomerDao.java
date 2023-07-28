package com.harimahat.customer;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;
import java.util.Optional;

/**
 * @Author hari.mahat on 27.7.2023
 * Project learn-spring3
 */

public interface CustomerDao {

    List<Customer> selectAllCustomers();

    Optional <Customer> selectCustomerById(Integer customerId);

    void insertCustomer( Customer customer);

    void  deleteCustomerById(Integer customerId);

    void  updateCustomer(Customer update);

    boolean existPersonWithEmail(String emailcustomer);

    boolean existPersonWithId(Integer customerId);



}
