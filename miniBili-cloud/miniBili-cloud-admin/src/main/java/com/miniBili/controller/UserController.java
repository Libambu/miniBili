package com.miniBili.controller;

import com.miniBili.api.consumer.WebClient;
import com.miniBili.entity.query.UserInfoQuery;
import com.miniBili.entity.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController extends ABaseController{

    @Autowired
    private WebClient webClient;

    @RequestMapping("/loadUser")
    public ResponseVO loadUser(UserInfoQuery userInfoQuery){
        userInfoQuery.setOrderBy("join_time desc");
        return getSuccessResponseVO(webClient.findListByPage(userInfoQuery));
    }
}
