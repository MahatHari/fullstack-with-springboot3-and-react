package com.harimahat.customer;

import com.harimahat.exception.DuplicateResourceException;
import com.harimahat.exception.RequestValidationException;
import com.harimahat.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @Author hari.mahat on 2.8.2023
 * Project learn-spring3
 */
//@ExtendWith(MockitoExtension.class) //this annotation will add MockitoAnnotations.openMocks, no need to add on setUp if used this
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable=   MockitoAnnotations.openMocks(this);
        underTest= new CustomerService(customerDao);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAllCustomers() {

        // WHEN
        underTest.getAllCustomers();
        // THEN
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomerById() {
        // GIVEN
        Long id= 2L;
        Customer customer= new Customer(id, "Har", "example@example.com", 23);


        Mockito.when(
                customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer)
                );
        // WHEN
        Customer actual= underTest.getCustomerById(id);
        // THEN
        assertThat(actual).isEqualTo(customer);
    }
    @Test
    void willThrowWhenGetCustomerByIdReturnEmptyOptional() {
        // GIVEN
        Long id= 2L;

        Mockito.when(
                        customerDao.selectCustomerById(id))
                .thenReturn(Optional.empty()
                );
        // WHEN

        // THEN
        assertThatThrownBy(
                ()->underTest.getCustomerById(id))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "Customer with id [%s] not found".formatted(id)
                );
    }

    @Test
    void addCustomer() {
        // GIVEN
        Long id=23L;
        String email="example@example.com";
        CustomerRegistrationRequest request= new CustomerRegistrationRequest("Har", "example@example.com", 23);

        when(customerDao.existPersonWithEmail(email)).thenReturn(false);
        // WHEN

        underTest.addCustomer(request);
        // THEN
        ArgumentCaptor<Customer> customerArgumentCaptor= ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer= customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void addCustomerWillThrowDuplicateResourceExceptionOnExistingEmail(){
        //Given
         String email="example@example.com";
        CustomerRegistrationRequest request= new CustomerRegistrationRequest("Har", "example@example.com", 23);
        //When

        when(customerDao.existPersonWithEmail(email)).thenReturn(true);
        //THEN
        assertThatThrownBy(()->underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("User with this  email already taken");

        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerById() {
        // GIVEN
        Long id=2L;
        when(customerDao.existPersonWithId(id)).thenReturn(true);
        // WHEN
        underTest.deleteCustomerById(id);
        // THEN
       verify(customerDao).deleteCustomerById(id);
    }
    @Test
    void deleteCustomerByIdWillThrowResourceNotFoundExceptionIfNoId() {
        // GIVEN
        Long id=2L;
        // WHEN
        when(customerDao.existPersonWithId(id)).thenReturn(false);

        // THEN
        assertThatThrownBy(
                ()->underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with [%s] not found".formatted(id));

        verify(customerDao, never()).deleteCustomerById(id);
    }
    @Test
    void updateCustomer() {
        // GIVEN
        Long id=2L;
        Customer customer= new Customer(id, "Hari", "example@example.com", 32);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail="noexam@example.com";
        CustomerUpdateRequest updateRequest= new CustomerUpdateRequest("hari",  newEmail,23);

        // WHEN

        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);
        when(customerDao.existPersonWithId(id)).thenReturn(true);
        underTest.updateCustomerById(id, updateRequest );
        // THEN
        ArgumentCaptor<Customer> customerArgumentCaptor= ArgumentCaptor.forClass(Customer.class);
       verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
       Customer capturedCustomer= customerArgumentCaptor.getValue();
       assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());


    }
    @Test
    void updateCustomerWhenGivenIdDoesNotExistThrowsResourceNotFoundException() {
        // GIVEN
        Long id=2L;
        //Customer customer= new Customer(id, "Hari", "example@example.com", 32);
        //TODO: to test thrown Exception,
        // Re-arrange code in CustomerService, only call  Customer customer= getCustomerById(customerId);
        // when Exceptions are not thrown by customerDao.existPersonWithId(customerId)
        //when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail="noexam@example.com";
        CustomerUpdateRequest updateRequest= new CustomerUpdateRequest("hari",  newEmail,23);

        // WHEN

        when(customerDao.existPersonWithId(id)).thenReturn(false);

        // THEN
        assertThatThrownBy(
                ()->underTest.updateCustomerById(id, updateRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] does not exists".formatted(id));

        verify(customerDao, never()).updateCustomer(any());


    }



    @Test
    void updateCustomerByIdOnlyName() {
        // GIVEN
        Long id=2L;
        Customer customer= new Customer(id, "Hari", "example@example.com", 32);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existPersonWithId(id)).thenReturn(true);
        CustomerUpdateRequest updateRequest= new CustomerUpdateRequest("hari",  null,null);
        // WHEN
        underTest.updateCustomerById(id,updateRequest );
        // THEN
        ArgumentCaptor<Customer> customerArgumentCaptor= ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer=  customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name()); // only Name updated
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());

    }
    @Test
    void updateCustomerByIdOnlyEmail() {
        // GIVEN
        Long id=2L;
        Customer customer= new Customer(id, "Hari", "example@example.com", 32);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existPersonWithId(id)).thenReturn(true);
        String newEmail ="jackson@jolly.com";
        CustomerUpdateRequest updateRequest= new CustomerUpdateRequest(null,  newEmail,null);
        // WHEN
        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);
        underTest.updateCustomerById(id,updateRequest );
        // THEN
        ArgumentCaptor<Customer> customerArgumentCaptor= ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer=  customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName()); // only Name updated
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());

    }
    @Test
    void updateCustomerByIdOnlyAge() {
        // GIVEN
        Long id=2L;
        Customer customer= new Customer(id, "Hari", "example@example.com", 32);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existPersonWithId(id)).thenReturn(true);

        CustomerUpdateRequest updateRequest= new CustomerUpdateRequest(null,  null,23);
        // WHEN
        underTest.updateCustomerById(id,updateRequest );
        // THEN
        ArgumentCaptor<Customer> customerArgumentCaptor= ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer=  customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName()); // only Name updated
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());

    }@Test
    void updateCustomerByIdWillThrowDuplicateResourceExceptionOnExistingEmail() {
        // GIVEN
        Long id=2L;
        Customer customer= new Customer(id, "Hari", "example@example.com", 32);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existPersonWithId(id)).thenReturn(true);

        String sameEmail ="example@example.com";

        CustomerUpdateRequest updateRequest= new CustomerUpdateRequest("Krishna",  sameEmail,null);
        when(customerDao.existPersonWithEmail(sameEmail)).thenReturn(true);
        // WHEN
       assertThatThrownBy(
               ()->underTest.updateCustomerById(id, updateRequest))
               .isInstanceOf(DuplicateResourceException.class)
               .hasMessage("email already taken");
        // THEN

        verify(customerDao, never()).updateCustomer(any());


    }
    @Test
    void updateCustomerByIdWithNoChangeWillThrowRequestValidationException() {
        // GIVEN
        Long id=2L;
        Customer customer= new Customer(id, "Hari", "example@example.com", 32);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existPersonWithId(id)).thenReturn(true);

        String sameEmail ="example@example.com";

        CustomerUpdateRequest updateRequest= new CustomerUpdateRequest("Hari", "example@example.com", 32);
        //when(customerDao.existPersonWithEmail(sameEmail)).thenReturn(true);
        // WHEN
        assertThatThrownBy(
                ()->underTest.updateCustomerById(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");
        // THEN

        verify(customerDao, never()).updateCustomer(any());


    }
}