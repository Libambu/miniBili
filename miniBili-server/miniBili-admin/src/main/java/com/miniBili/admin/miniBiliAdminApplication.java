package com.miniBili.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.miniBili.admin"})
public class miniBiliAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(miniBiliAdminApplication.class,args);
    }
}
