package com.miniBili.api.provider;


import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.po.UserInfo;
import com.miniBili.mappers.UserInfoMapper;
import com.miniBili.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class UserInfoApi {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @RequestMapping(Constants.Inner_api_prefix + "/user/updateCoinCountInfo")
    Integer updateCoinCountInfo(@RequestParam String userId, @RequestParam Integer count){
        Integer updateCount = userInfoMapper.updateCoinCountInfo(userId,count);
        return updateCount;
    }

    @RequestMapping(Constants.Inner_api_prefix + "/user/getUserInfoByUserId")
    UserInfo getUserInfoByUseId(@RequestParam String userId){
        UserInfo userInfoByUserId = userInfoService.getUserInfoByUserId(userId);
        return  userInfoByUserId;
    }
}
