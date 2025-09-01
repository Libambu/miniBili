package com.miniBili.entity.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AppConfig {
    @Value("${project.folder}")
    private String projectFolder;
    @Value("${admin.account}")
    private String AdminAccount;
    @Value("${admin.password}")
    private String adminPassword;
}
