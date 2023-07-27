package com.harimahat.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author hari.mahat on 27.7.2023
 * Project learn-spring3
 */

@Repository
public class CustomerDataAccessService implements  CustomerDao{



    private final List<Customer> customers ;

    public CustomerDataAccessService() {
        this.customers= new ArrayList<>();
        Customer alex=  new Customer(1, "Alex", "alex@gmailc.om", 29);
        Customer john=  new Customer(2, "John", "john@gmailc.om", 49);
        Customer alexa=  new Customer(3, "Alexa", "alexa@gmailc.om", 19);

        customers.add(alex);
        customers.add(john);
        customers.add(alexa);
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }
    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        return customers
                .stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst();
    }
}
