package com.miniBili.filter;

import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.enums.ResponseCodeEnum;
import com.miniBili.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器
 */
@Slf4j
@Component
public class GatewayGlobalRequestFilter implements GlobalFilter , Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String rawPath = exchange.getRequest().getURI().getRawPath();
        log.info("请求的路径为,{}",rawPath);
        if(rawPath.indexOf(Constants.Inner_api_prefix)!=-1){
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
