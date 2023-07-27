package com.harimahat.customer;

import java.util.List;
import java.util.Optional;

/**
 * @Author hari.mahat on 27.7.2023
 * Project learn-spring3
 */

public interface CustomerDao {

    public List<Customer> selectAllCustomers();

    Optional <Customer> selectCustomerById(Integer customerId);
}
