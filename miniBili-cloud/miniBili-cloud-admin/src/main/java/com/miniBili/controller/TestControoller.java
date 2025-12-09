package com.miniBili.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestControoller {

    @GetMapping("/test")
    public String test(){
        return "我的微服务admin的第一个接口";
    }
}
