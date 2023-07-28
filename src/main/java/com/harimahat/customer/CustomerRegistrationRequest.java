package com.harimahat.customer;

/**
 * @Author hari.mahat on 28.7.2023
 * Project learn-spring3
 */
public record CustomerRegistrationRequest (
    String name,
    String email,
    Integer age
){};
