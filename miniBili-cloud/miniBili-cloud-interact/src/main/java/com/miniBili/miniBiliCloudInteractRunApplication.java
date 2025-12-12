package com.miniBili;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class miniBiliCloudInteractRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(miniBiliCloudInteractRunApplication.class,args);
    }
}
