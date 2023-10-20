package com.example.ceng453_20231_group11_backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    @GetMapping
    public String returnHelloWorld(){
        return "Hello World";
    }
}