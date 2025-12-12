package com.miniBili;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class miniBiliCloudAdminRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(miniBiliCloudAdminRunApplication.class,args);
    }
}
