package com.miniBili.admin.intercept;

import com.miniBili.component.RedisComponent;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.enums.ResponseCodeEnum;
import com.miniBili.exception.BusinessException;
import com.miniBili.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Component
public class AppIntercept implements HandlerInterceptor {
    @Autowired
    private RedisComponent redisComponent;

    @Override//目标资源方法运行前运行，返回true：放行，返回false，不放行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler==null){
            return false;
        }
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        if(request.getRequestURI().contains("/account")){
            return true;
        }

        String token = request.getHeader(Constants.TOKEN_ADMIN);
        //获取图片
        if(request.getRequestURI().contains("/file")){
            //没办法从hander中取
            token = getTokenFromCookies(request);
        }
        if(StringTools.isEmpty(token)){
            throw  new BusinessException(ResponseCodeEnum.CODE_901);
        }

        String account = redisComponent.getToken4Admin(token);
        if(account==null){
            throw  new BusinessException(ResponseCodeEnum.CODE_901);
        }

        return true;
    }

    private String getTokenFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies==null){
            return null;
        }
        for(Cookie c :cookies){
            if(c.getName().equals(Constants.TOKEN_WEB)){
                return c.getValue();
            }
        }

        return null;
    }
}

