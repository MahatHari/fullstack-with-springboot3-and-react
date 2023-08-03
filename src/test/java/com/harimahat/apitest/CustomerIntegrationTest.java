package com.harimahat.apitest;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.harimahat.customer.Customer;
import com.harimahat.customer.CustomerRegistrationRequest;
import com.harimahat.customer.CustomerUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author hari.mahat on 2.8.2023
 * Project learn-spring3
 */
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT )
public class CustomerIntegrationTest {

    //TODO: 1. create registration request,
    // 2. send a post request,
    // 3. get all customers,
    // 4.make sure that customer is present
    // 5. getCustomerById

    private static final Random RANDOM= new Random();
    private static final String CUSTOMER_URI="api/v1/customers";
    @Autowired
    private WebTestClient webTestClient;


    @Test
    void registerCustomer() {
        Faker faker= new Faker();
        Name fakerName= faker.name();
        String name= fakerName.fullName();
        String email= fakerName.lastName()+ UUID.randomUUID()+"@hmtest.com";
        int age=  RANDOM.nextInt(1,99);


        // GIVEN
        CustomerRegistrationRequest request= new CustomerRegistrationRequest(
            name, email, age
        );


        // DONE: register customer
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //DONE: get all customers
        List<Customer> allCustomers= webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        // DONE: Make sure added customer is present
        Customer expectedCustomer= new Customer(name, email, age);

        assertThat(allCustomers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);


        // Done: get customer by Id

        // Get id of Expected customer, todo change it later to better
        assert allCustomers != null;
        var id= allCustomers.stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        expectedCustomer.setId(id);

        //Done performing getMapping with id
        webTestClient.get()
                .uri(CUSTOMER_URI+"/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expectedCustomer);



    }

    @Test
    void deleteCustomer(){

        Faker faker= new Faker();
        Name fakerName= faker.name();
        String name= fakerName.fullName();
        String email= fakerName.lastName()+ UUID.randomUUID()+"@hmtest.com";
        int age=  RANDOM.nextInt(1,99);


        // GIVEN
        CustomerRegistrationRequest request= new CustomerRegistrationRequest(
                name, email, age
        );


        // DONE: register customer
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //DONE: get all customers
        List<Customer> allCustomers= webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        // Done: get customer by Id

        // Get id of Expected customer, todo change it later to better
        assert allCustomers != null;
        var id= allCustomers.stream()
                .filter(c->c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();



        //Done performing deleteMapping with id
        webTestClient.delete()
                .uri(CUSTOMER_URI+"/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(CUSTOMER_URI+"/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void updateCustomer(){

        Faker faker= new Faker();
        Name  name= faker.name();
        String firstname= name.firstName();
        String lastName= name.lastName();
        String fullName=firstname+" "+lastName;
        String email= lastName+UUID.randomUUID()+"@test-mail.com";
        int age=23;

        CustomerRegistrationRequest request= new CustomerRegistrationRequest(fullName, email, age);

        webTestClient.post()
                .uri(CUSTOMER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

       List<Customer> customerList= webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


       // get Id to a customer we just posted above to update
        assert customerList!=null;

        Long Id= customerList
                .stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

       // done update name
        String toUpdateName= "Hari Mahat";
        CustomerUpdateRequest updateRequest= new CustomerUpdateRequest(toUpdateName, null, null);

        webTestClient.put()
                .uri(CUSTOMER_URI+"/{id}", Id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

       Customer customer= webTestClient.get()
                .uri(CUSTOMER_URI+"/{id}",Id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

       Customer actualCustomerAfterUpdate= new Customer(Id, toUpdateName,email,age);

       // Verify
       assert customer !=null;
     assertThat(actualCustomerAfterUpdate).isEqualTo(customer);

    }

}
