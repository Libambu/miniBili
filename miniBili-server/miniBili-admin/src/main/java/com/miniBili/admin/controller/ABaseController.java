package com.miniBili.admin.controller;
import com.miniBili.component.RedisComponent;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.enums.ResponseCodeEnum;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ABaseController {

    protected static final String STATUC_SUCCESS = "success";

    protected static final String STATUC_ERROR = "error";

    @Autowired
    private RedisComponent redisComponent;

    protected <T> ResponseVO getSuccessResponseVO(T t) {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setStatus(STATUC_SUCCESS);
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVO.setData(t);
        return responseVO;
    }

    protected <T> ResponseVO getBusinessErrorResponseVO(BusinessException e, T t) {
        ResponseVO vo = new ResponseVO();
        vo.setStatus(STATUC_ERROR);
        if (e.getCode() == null) {
            vo.setCode(ResponseCodeEnum.CODE_600.getCode());
        } else {
            vo.setCode(e.getCode());
        }
        vo.setInfo(e.getMessage());
        vo.setData(t);
        return vo;
    }

    protected <T> ResponseVO getServerErrorResponseVO(T t) {
        ResponseVO vo = new ResponseVO();
        vo.setStatus(STATUC_ERROR);
        vo.setCode(ResponseCodeEnum.CODE_500.getCode());
        vo.setInfo(ResponseCodeEnum.CODE_500.getMsg());
        vo.setData(t);
        return vo;
    }


    protected void cleanCookie(HttpServletResponse response){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies==null){
            return;
        }
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(Constants.TOKEN_ADMIN)){
                token=cookie.getValue();
                redisComponent.cleanToken4Admin(token);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                break;
            }
        }

    }

    protected void saveCookieAdmin(HttpServletResponse response,String token){
        Cookie cookie = new Cookie(Constants.TOKEN_ADMIN,token);
        //cookie.setMaxAge((Constants.REDIS_KEY_EXPIRE_ONE_DAY/1000));
        //关闭浏览器后Cookie就失效会话
        cookie.setMaxAge(-1);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
