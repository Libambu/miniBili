package com.miniBili.entity.config;

import lombok.Data;
import org.elasticsearch.client.security.user.privileges.ApplicationPrivilege;
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
    @Value("${es.host.port:127.0.0.1:9200}")
    private String esHostPort;
    @Value("${es.index.video.name:minibili_video}")
    private String esIndexName;
}
