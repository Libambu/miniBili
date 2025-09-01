package com.miniBili.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.miniBili"})
@MapperScan(basePackages = {"com.miniBili.mappers"})
@EnableTransactionManagement
@EnableScheduling
public class miniBiliAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(miniBiliAdminApplication.class,args);
    }
}
