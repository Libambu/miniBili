package com.miniBili.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.miniBili.web"})

public class miniBiliWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(miniBiliWebApplication.class,args);
    }
}
