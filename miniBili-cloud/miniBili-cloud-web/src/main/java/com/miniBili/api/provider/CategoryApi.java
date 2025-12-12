package com.miniBili.api.provider;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryApi {

    @RequestMapping("/loadAllCategory")
    public String loadAllCategory(){
        return "这是admin提供的分类接口";
    }
}
