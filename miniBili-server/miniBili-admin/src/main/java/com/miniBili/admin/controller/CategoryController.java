package com.miniBili.admin.controller;

import com.miniBili.entity.vo.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/category")
public class CategoryController extends ABaseController{

    @RequestMapping("/loadCategory")
    public ResponseVO loadCategory(){
        return getSuccessResponseVO(null);
    }

}
