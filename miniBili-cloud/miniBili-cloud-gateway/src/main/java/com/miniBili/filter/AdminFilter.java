package com.miniBili.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AdminFilter extends AbstractGatewayFilterFactory {

    @Override
    public GatewayFilter apply(Object config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request =  exchange.getRequest();
            String rawPath = request.getURI().getRawPath();
            log.info("admin:"+rawPath);
            //TODO 管理端登录校验
            return chain.filter(exchange);
        });
    }
}
