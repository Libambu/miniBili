package com.miniBili;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class miniBiliCloudWebRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(miniBiliCloudWebRunApplication.class,args);
    }
}
