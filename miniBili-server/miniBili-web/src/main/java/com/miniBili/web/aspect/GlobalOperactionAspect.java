package com.miniBili.web.aspect;


import com.miniBili.web.annotatioon.GlobalIntercept;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.enums.ResponseCodeEnum;
import com.miniBili.exception.BusinessException;
import com.miniBili.redis.RedisUtils;
import com.miniBili.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class GlobalOperactionAspect {

    @Autowired
    private RedisUtils redisUtils;

    public GlobalOperactionAspect(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    /**
     * 定义切入点
     * @param point
     */
    @Before("@annotation(com.miniBili.web.annotatioon.GlobalIntercept)")
    public void interceptDo(JoinPoint point){
        Method method = ((MethodSignature)point.getSignature()).getMethod();
        GlobalIntercept intercept = method.getAnnotation(GlobalIntercept.class);
        if(intercept==null){
            return;
        }
        if(intercept.checkLogin()){
            checkLogin();
        }
    }

    /**
     * 椒盐登录
     */
    private void checkLogin(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(Constants.TOKEN_WEB);
        if(StringTools.isEmpty(token)){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        TokenInfoDto tokenInfoDto = (TokenInfoDto) redisUtils.get(Constants.REDIS_KEY_TOKEN_WEB + token);
        if(tokenInfoDto==null){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
    }
}
