package com.miniBili.api.provider;

import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.po.UserAction;
import com.miniBili.entity.query.UserActionQuery;
import com.miniBili.service.UserActionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@Slf4j
public class UserActionApi {

    @Autowired
    private   UserActionService userActionService;

    @RequestMapping(Constants.Inner_api_prefix + "/userAction/findListByParam")
    List<UserAction> getUserActionList(@RequestBody UserActionQuery userActionQuery){
        return userActionService.findListByParam(userActionQuery);
    }
}
