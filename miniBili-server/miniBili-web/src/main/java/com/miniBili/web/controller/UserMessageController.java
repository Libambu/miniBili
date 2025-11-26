package com.miniBili.web.controller;


import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.query.UserMessageQuery;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.service.UserMessageService;
import com.oracle.js.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userMessage")
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
}
