package com.miniBili.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {
    @RequestMapping("test")
    public String test(){
        return "web模块测试成功";
    }
}
