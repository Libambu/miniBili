package com.miniBili.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {
    @RequestMapping("test")
    public String test(){
        return "admin模块测试成功";
    }
}
