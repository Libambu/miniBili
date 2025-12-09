package com.miniBili.handler;


import com.miniBili.entity.enums.ResponseCodeEnum;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.exception.BusinessException;
import com.miniBili.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;


import java.nio.charset.StandardCharsets;


/**
 * http://localhost:7071/admin/test2这种路径是给到具体服务了，不走这里
 * http://localhost:7071/admin2/test这种就会走这个gateWay的异常处理了
 */
@Slf4j
@Component
@Order(-1)
public class GateWayExceptionHandler implements WebExceptionHandler {

    private static final String STATUC_ERROR = "error";

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ResponseVO responseVO = getResponse(exchange,ex);
        DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(JsonUtils.converObj2Json(responseVO).getBytes(StandardCharsets.UTF_8));
        return serverHttpResponse.writeWith(Mono.just(dataBuffer));
    }

    private ResponseVO getResponse(ServerWebExchange exchange, Throwable ex){
        ResponseVO ajaxResponse = new ResponseVO();
        ajaxResponse.setStatus(STATUC_ERROR);
        if(ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            if(HttpStatus.NOT_FOUND==responseStatusException.getStatus()){
                ajaxResponse.setCode(ResponseCodeEnum.CODE_404.getCode());
                ajaxResponse.setInfo(ResponseCodeEnum.CODE_404.getMsg());
                return ajaxResponse;
            }else if(HttpStatus.SERVICE_UNAVAILABLE==responseStatusException.getStatus()){
                //某个微服务挂了导致的服务不可用
                ajaxResponse.setCode(ResponseCodeEnum.CODE_503.getCode());
                ajaxResponse.setInfo(ResponseCodeEnum.CODE_503.getMsg());
                return ajaxResponse;
            }else{
                //某个微服务挂了导致的服务不可用
                ajaxResponse.setCode(responseStatusException.getStatus().value());
                ajaxResponse.setInfo(ResponseCodeEnum.CODE_500.getMsg());
                return ajaxResponse;
            }
        }else if(ex instanceof BusinessException){
            BusinessException biz = (BusinessException) ex;
            ajaxResponse.setCode(biz.getCode() == null ? ResponseCodeEnum.CODE_600.getCode() : biz.getCode());
            ajaxResponse.setInfo(biz.getMessage());
            ajaxResponse.setStatus(STATUC_ERROR);
            return ajaxResponse;
        }
        ajaxResponse.setCode(ResponseCodeEnum.CODE_500.getCode());
        ajaxResponse.setInfo(ResponseCodeEnum.CODE_500.getMsg());
        return ajaxResponse;
    }
}
