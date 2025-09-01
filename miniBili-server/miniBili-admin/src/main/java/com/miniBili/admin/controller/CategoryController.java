package com.miniBili.admin.controller;

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
@RequestMapping("/admin/category")
public class CategoryController extends ABaseController{

    @Autowired
    private CategoryInfoService categoryInfoService;

    @RequestMapping("/loadCategory")
    public ResponseVO loadCategory(CategoryInfoQuery categoryInfoQuery){
        categoryInfoQuery.setOrderBy("sort asc");
        categoryInfoQuery.setConvert2Tree(true);
        List<CategoryInfo> categoryInfoList = categoryInfoService.findListByParam(categoryInfoQuery);
        return getSuccessResponseVO(categoryInfoList);
    }

    @RequestMapping("/saveCategory")
    public ResponseVO saveCategory(@NotEmpty Integer pCategoryId,
                                   Integer categoryId,
                                   @NotEmpty String categoryCode,
                                   @NotEmpty String categoryName,
                                   String icon,
                                   String background){

        CategoryInfo categoryInfo = new CategoryInfo();
        categoryInfo.setCategoryId(categoryId);
        categoryInfo.setCategoryCode(categoryCode);
        categoryInfo.setCategoryName(categoryName);
        categoryInfo.setIcon(icon);
        categoryInfo.setBackground(background);
        categoryInfo.setpCategoryId(pCategoryId);

        categoryInfoService.saveCategory(categoryInfo);
        return getSuccessResponseVO(null);
    }
    @RequestMapping("/delCategory")
    public ResponseVO delCategory(@NotEmpty Integer categoryId){
        categoryInfoService.delCategory(categoryId);
        return getSuccessResponseVO(null);
    }


    @RequestMapping("/changeSort")
    public ResponseVO changeSort(@NotNull Integer pCategoryId, @NotEmpty String categoryIds){
        categoryInfoService.changeSort(pCategoryId,categoryIds);
        return getSuccessResponseVO(null);
    }
}
