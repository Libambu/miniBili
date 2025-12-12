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
 * *   **普通 Spring Boot (MVC):** 基于 **Servlet API**，是阻塞式 IO。
 *      通常使用 `@ControllerAdvice` + `@ExceptionHandler` 全局捕获。
 * *   **Spring Cloud Gateway:** 基于 **WebFlux (Project Reactor + Netty)**，是非阻塞响应式编程。
 *      Servlet API 在这里不存在，因此传统的捕获方式往往无法拦截到 Gateway 核心过滤器（Filter）或路由（Routing）层面的异常。
 *
 */

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
        //是 WebFlux 的核心。在响应式编程里，你不能直接“返回结果”，而是要返回一个“任务（Publisher）”。
        // `Mono<Void>` 的意思就是：**“这是一个异步任务，任务执行完不需要返回具体数据，只要告诉系统‘我做完了’就行”**
        // （因为数据通过 Response 写入流里了）。
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        //告诉前端：“我要给你返回 JSON 格式的数据”。
        serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ResponseVO responseVO = getResponse(exchange,ex);
        //以前在 Servlet 里，我们直接 `response.getWriter().write(String)`。
        //但在 Gateway (Netty) 底层，数据不是字符串，而是**字节流**。
        //wrap(...)`**：把你的 JSON 字节数组（`byte[]`），**包装**成一个 Netty 能看懂的**数据块**（叫 `DataBuffer`）
        DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(JsonUtils.converObj2Json(responseVO).getBytes(StandardCharsets.UTF_8));
        return serverHttpResponse.writeWith(Mono.just(dataBuffer));
    }

    /**
     *Mono<Void>函数的返回值是这个，但是为什么最后返回的是return serverHttpResponse.writeWith(Mono.just(dataBuffer));
     ***`Mono<String>`**：代表一个任务，做完后会给你一个字符串（比如查询数据库名字）。
     ***`Mono<Void>`**：代表一个任务，做完后**什么数据都不给你，只告诉你“我做完了”**。
     */

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
