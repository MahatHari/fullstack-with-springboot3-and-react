package com.harimahat.customer;

import com.harimahat.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author hari.mahat on 27.7.2023
 * Project learn-spring3
 */
@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return  customerDao.selectAllCustomers();
    };

    public Customer getCustomerById(Integer customerId){
        return customerDao.selectCustomerById(customerId)
                .orElseThrow(()->new ResourceNotFoundException("Customer with id [%s] not found".formatted(customerId)));
    }
}
