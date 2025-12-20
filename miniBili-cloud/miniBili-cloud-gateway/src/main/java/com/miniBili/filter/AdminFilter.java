package com.miniBili.filter;


import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.enums.ResponseCodeEnum;
import com.miniBili.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.StripPrefixGatewayFilterFactory;
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
            if(rawPath.contains("account")){
                return chain.filter(exchange);
            }
            String token = getToken(request);
            if(rawPath.contains("file")){
                token = getTokenFromCookie(request);
            }
            if(token.isEmpty()){
                throw new BusinessException(ResponseCodeEnum.CODE_901);
            }
            return chain.filter(exchange);
        });
    }

    private String getToken(ServerHttpRequest request){
        return request.getHeaders().getFirst(Constants.TOKEN_ADMIN);
    }

    private String getTokenFromCookie(ServerHttpRequest request){
        return request.getCookies().getFirst(Constants.TOKEN_ADMIN).getValue();
    }


}
