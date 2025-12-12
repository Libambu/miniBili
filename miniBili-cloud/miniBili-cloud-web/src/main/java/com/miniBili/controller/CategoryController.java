package com.miniBili.controller;

import com.miniBili.api.consumer.CategoryClient;
import com.miniBili.entity.po.CategoryInfo;
import com.miniBili.entity.query.CategoryInfoQuery;
import com.miniBili.entity.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController extends ABaseController{

    @Autowired
    private CategoryClient categoryClient;

    @RequestMapping("/loadAllCategory")
    public ResponseVO loadCategory(){
        List<CategoryInfo> categoryInfoList =categoryClient.loadAllCategory();
        //List<CategoryInfo> categoryInfoList = categoryInfoService.getAllCategoryList();
        return getSuccessResponseVO(categoryInfoList);
    }

}
