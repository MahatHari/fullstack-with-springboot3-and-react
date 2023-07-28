package com.harimahat;

import com.harimahat.customer.Customer;
import com.harimahat.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
            Customer alex=  new Customer( "Alex", "alex@gmailc.om", 29);
            Customer john=  new Customer( "John", "john@gmailc.om", 49);
            Customer alexa=  new Customer( "Alexa", "alexa@gmailc.om", 19);

            List<Customer>customers= List.of(alex, john, alexa);
            customerRepository.saveAll(customers);
        };
    }

}

