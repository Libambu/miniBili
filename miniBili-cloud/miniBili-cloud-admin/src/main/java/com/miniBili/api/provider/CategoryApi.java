package com.miniBili.api.provider;


import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.po.CategoryInfo;
import com.miniBili.entity.query.CategoryInfoQuery;
import com.miniBili.service.CategoryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Constants.Inner_api_prefix)
public class CategoryApi {

    @Autowired
    private CategoryInfoService categoryInfoService;

    @RequestMapping("/loadAllCategory")
    public List<CategoryInfo> loadAllCategory(){
        List<CategoryInfo> categoryInfoList = categoryInfoService.getAllCategoryList();
        return categoryInfoList;
    }
}
