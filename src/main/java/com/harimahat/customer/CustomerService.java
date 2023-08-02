package com.harimahat.customer;

import com.harimahat.exception.DuplicateResourceException;
import com.harimahat.exception.RequestValidationException;
import com.harimahat.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author hari.mahat on 27.7.2023
 * Project learn-spring3
 */
@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return  customerDao.selectAllCustomers();
    };

    public Customer getCustomerById(Long customerId){
        return customerDao.selectCustomerById(customerId)
                .orElseThrow(()->new ResourceNotFoundException("Customer with id [%s] not found".formatted(customerId)));
    }

    public void addCustomer(CustomerRegistrationRequest request){
        // check if email exists
        if(customerDao.existPersonWithEmail(request.email())){
            throw  new DuplicateResourceException("User with this  email already taken");
        }
        Customer customer= new Customer(request.name(), request.email(), request.age());
        // If it does not exist add
        customerDao.insertCustomer(customer);
    }

    public void deleteCustomerById(Long customerId){

        if (!customerDao.existPersonWithId(customerId)){
            throw new ResourceNotFoundException("Customer with [%s] not found".formatted(customerId));
        }
        customerDao.deleteCustomerById(customerId);

        /*Customer customer= getCustomerById(customerId);
        if(customer!=null){
            customerDao.deleteCustomer(customer);
        }*/
    }

    public  void updateCustomerById(Long customerId, CustomerUpdateRequest updateRequest){
        boolean changes=false;

        if(!customerDao.existPersonWithId(customerId)){
            throw  new ResourceNotFoundException("Customer with id [%s] does not exists".formatted(customerId));
        }

       Customer customer= getCustomerById(customerId);

        if(updateRequest.name()!=null && !updateRequest.name().equals(customer.getName())){
            customer.setName(updateRequest.name());

            changes=true;
        }
        if(updateRequest.email()!=null ){
            if(customerDao.existPersonWithEmail(updateRequest.email())){
                throw new DuplicateResourceException("email already taken");
            }
            if(!updateRequest.email().equals(customer.getEmail())){
            customer.setEmail(updateRequest.email());
            changes=true;
            }
        }
        if(updateRequest.age() != null && !(updateRequest.age().equals(customer.getAge()))){
            customer.setAge(updateRequest.age());
            changes=true;
        }
        if(!changes){
           throw  new RequestValidationException("no data changes found");
        }
        customerDao.updateCustomer(customer);
    }

}
