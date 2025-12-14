package com.miniBili.controller;


import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.dto.UserMessageCount;
import com.miniBili.entity.po.UserMessage;
import com.miniBili.entity.query.UserMessageQuery;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/message")
@Validated
public class UserMessageController extends ABaseController{

    @Autowired
    private UserMessageService userMessageService;

    @RequestMapping("/getNoReadCount")
    public ResponseVO getNoReadCount(){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        UserMessageQuery query = new UserMessageQuery();
        query.setUserId(tokenInfoDto.getUserId());
        query.setReadType(0);
        Integer count = userMessageService.findCountByParam(query);
        return getSuccessResponseVO(count);
    }

    @RequestMapping("/getNoReadCountGroup")
    public ResponseVO  getNoReadCountGroup(){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        List<UserMessageCount>dataList = userMessageService.getMessageTypeNoReadCount(tokenInfoDto.getUserId());
        return getSuccessResponseVO(dataList);
    }

    @RequestMapping("/readAll")
    public ResponseVO  readAll(Integer messageType){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        UserMessageQuery userMessageQuery = new UserMessageQuery();
        userMessageQuery.setUserId(tokenInfoDto.getUserId());
        userMessageQuery.setMessageType(messageType);
        UserMessage userMessage = new UserMessage();
        userMessage.setReadType(1);
        userMessageService.updateByParam(userMessage,userMessageQuery);
        return getSuccessResponseVO(null);
    }

    /**
     * 加载消息
     * @param messageType
     * @param pageNo
     * @return
     */
    @RequestMapping("/loadMessage")
    public ResponseVO  loadMessage(@NotNull Integer messageType,Integer pageNo){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        UserMessageQuery userMessageQuery = new UserMessageQuery();
        userMessageQuery.setMessageType(messageType);
        userMessageQuery.setUserId(tokenInfoDto.getUserId());
        userMessageQuery.setPageNo(pageNo);
        userMessageQuery.setOrderBy("message_id desc");
        PaginationResultVO resultVO = userMessageService.findListByPage(userMessageQuery);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/delMessage")
    public ResponseVO  delMessage(@NotNull Integer messageId){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        UserMessageQuery userMessageQuery = new UserMessageQuery();
        userMessageQuery.setMessageId(messageId);
        userMessageQuery.setUserId(tokenInfoDto.getUserId());
        userMessageService.deleteByParam(userMessageQuery);
        return null;
    }





}
