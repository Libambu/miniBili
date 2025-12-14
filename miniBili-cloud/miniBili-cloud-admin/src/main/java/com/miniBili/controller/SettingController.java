package com.miniBili.controller;


import com.miniBili.component.RedisComponent;
import com.miniBili.entity.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/setting")
@Validated
@Slf4j
public class SettingController extends ABaseController{

    @Autowired
    private RedisComponent redisComponent;


    @RequestMapping("getSetting")
    public ResponseVO getSetting(){
        return getSuccessResponseVO(redisComponent.getSystemSetting());
    }
}
