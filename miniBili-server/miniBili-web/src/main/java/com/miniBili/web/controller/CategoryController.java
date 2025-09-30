package com.miniBili.web.controller;

import com.miniBili.entity.po.CategoryInfo;
import com.miniBili.entity.query.CategoryInfoQuery;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.service.CategoryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController extends ABaseController{

    @Autowired
    private CategoryInfoService categoryInfoService;

    @RequestMapping("/loadAllCategory")
    public ResponseVO loadCategory(CategoryInfoQuery categoryInfoQuery){
        List<CategoryInfo> categoryInfoList = categoryInfoService.getAllCategoryList();
        return getSuccessResponseVO(categoryInfoList);

    }

}
