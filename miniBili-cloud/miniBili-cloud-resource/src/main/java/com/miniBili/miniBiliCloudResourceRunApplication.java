package com.miniBili;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.miniBili",exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients
public class miniBiliCloudResourceRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(miniBiliCloudResourceRunApplication.class,args);
    }
}
