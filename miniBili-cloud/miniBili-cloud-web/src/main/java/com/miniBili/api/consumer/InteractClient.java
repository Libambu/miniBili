package com.miniBili.api.consumer;


import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.po.UserAction;
import com.miniBili.entity.query.UserActionQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = Constants.InteractName)
@Component
public interface InteractClient {

    @RequestMapping(Constants.Inner_api_prefix + "/userAction/findListByParam")
    List<UserAction> getUserActionList(@RequestBody UserActionQuery userActionQuery);
}
