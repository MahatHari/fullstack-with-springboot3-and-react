package com.harimahat;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.harimahat.customer.Customer;
import com.harimahat.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author hari.mahat on 27.7.2023
 * Project learn-spring3
 */
@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

    }
    @Bean
    CommandLineRunner runner( CustomerRepository customerRepository){
        return  args -> {

            Faker faker= new Faker();
            Random random= new Random();
            Name name= faker.name();
            String firstname=name.firstName();
            String lastname=name.lastName();
            Customer customer=  new Customer( firstname+" "+ lastname, firstname.toLowerCase()+"."+lastname.toLowerCase()+"@hm.com", random.nextInt(16,99));
            //  Customer john=  new Customer( "John", "john@gmailc.om", 49);
            //   Customer alexa=  new Customer( "Alexa", "alexa@gmailc.om", 19);

            // List<Customer>customers= List.of(alex, john, alexa);
            // customerRepository.saveAll(customers);

            customerRepository.save(customer);
        };
    }

}

