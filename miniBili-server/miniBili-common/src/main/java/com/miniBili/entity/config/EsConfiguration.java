package com.miniBili.entity.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class EsConfiguration {

    @Autowired
    private AppConfig appConfig;

    @Bean(destroyMethod = "close")   // 应用关闭时自动调用 client.close()
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(HttpHost.create(appConfig.getEsHostPort()))
        );
    }
}
