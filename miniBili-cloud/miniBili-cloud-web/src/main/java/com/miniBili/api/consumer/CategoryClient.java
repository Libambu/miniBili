package com.miniBili.api.consumer;

import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.po.CategoryInfo;
import com.miniBili.entity.query.CategoryInfoQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = Constants.AdminName)
@Component
public interface CategoryClient {

    @RequestMapping(Constants.Inner_api_prefix + "/loadAllCategory")
    List<CategoryInfo> loadAllCategory();

}
