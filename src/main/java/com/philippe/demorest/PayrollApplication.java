package com.philippe.demorest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;




/**
 * PayrollApplication
 */
@SpringBootApplication
@Slf4j
public class PayrollApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayrollApplication.class, args);
        log.debug("Hello, I'm DEBUG message.");
    }
    
}