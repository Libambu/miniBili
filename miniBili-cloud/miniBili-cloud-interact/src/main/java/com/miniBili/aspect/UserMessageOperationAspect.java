package com.miniBili.aspect;


import com.miniBili.annotation.RecordUserMessage;
import com.miniBili.component.RedisComponent;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.enums.MessageTypeEnum;
import com.miniBili.entity.enums.UserActionTypeEnum;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.service.UserMessageService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Component
@Slf4j
@Aspect
public class UserMessageOperationAspect {

    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private UserMessageService userMessageService;

    /**
     * 为什么要返回值呢？因为是环绕通知，包裹住要返回
     * @param joinPoint
     * @return
     */
    @Around("@annotation(com.miniBili.annotation.RecordUserMessage)")
    public ResponseVO interceptorDo(ProceedingJoinPoint joinPoint) throws Throwable {
        //执行被包裹的业务逻辑，返回他的结果
        ResponseVO responseVO = (ResponseVO)joinPoint.proceed();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RecordUserMessage recordUserMessage = method.getAnnotation(RecordUserMessage.class);
        if(recordUserMessage!=null){
            //注解、参数值、参数信息数组（可从其中获取参数名，参数注解，参数类型等等）
            saveMessage(recordUserMessage,joinPoint.getArgs(),method.getParameters());
        }
        return responseVO;
    }

    /**
     *
     * @param recordUserMessage 注解，记录了用户行为
     * @param args 参数值数组
     * @param parameters 参数信息数组，可以获取参数的注解，名称，属性等等
     */
    private void saveMessage(RecordUserMessage recordUserMessage,
                             Object[] args,
                             Parameter[] parameters){
        //首先解析出必要的信息
        String videoId = null;
        Integer actionType = null;
        Integer replyCommentId = null;
        String content = null;
        String reason = null;
        for(int i=0;i< parameters.length;i++){
            if(parameters[i].getName().equals("videoId")){
                videoId = (String) args[i];
            }else if(parameters[i].getName().equals("actionType")){
                actionType = (Integer) args[i];
            }else if(parameters[i].getName().equals("replyCommentId")){
                replyCommentId = (Integer) args[i];
            }else if(parameters[i].getName().equals("content")){
                content = (String) args[i];
            }else if(parameters[i].getName().equals("reason")){
                reason = (String) args[i];
            }
        }

        //因为用户操作包括点赞，收藏，但是注解用的是点赞，所以要单独处理一下收藏
        MessageTypeEnum messageTypeEnum = recordUserMessage.messageType();
        if(UserActionTypeEnum.VIDEO_COLLECT.getType().equals(actionType)){
            messageTypeEnum = MessageTypeEnum.COLLECTION;
        }
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        userMessageService.saveUserMessage(videoId,tokenInfoDto==null?null:tokenInfoDto.getUserId(),messageTypeEnum,content,replyCommentId);
    }

    private TokenInfoDto getTokenInfoDto(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(Constants.TOKEN_WEB);
        return redisComponent.getTokenInfoDtoByToken(token);
    }


}
