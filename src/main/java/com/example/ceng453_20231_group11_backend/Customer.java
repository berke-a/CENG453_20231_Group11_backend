package com.example.ceng453_20231_group11_backend;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {
    private Long customerId;

    private String firstName;

    private String lastName;

    private String email;
}