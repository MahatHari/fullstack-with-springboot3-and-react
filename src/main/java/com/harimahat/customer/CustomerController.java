package com.harimahat.customer;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @Author hari.mahat on 27.7.2023
 * Project learn-spring3
 */
@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(path = "api/v1/customers",method = GET)
    public List<Customer> getCustomers(){
        return customerService.getAllCustomers();
    }

    @RequestMapping(path = "api/v1/customers/{customerId}", method = GET)
    public Customer getCustomer(@PathVariable("customerId") Integer customerId){
        return customerService.getCustomerById(customerId);
    }
}
