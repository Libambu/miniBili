package com.miniBili.web.controller;

import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.po.UserInfo;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.entity.vo.UserVo;
import com.miniBili.service.UserActionService;
import com.miniBili.service.UserFocusService;
import com.miniBili.service.UserInfoService;
import com.miniBili.service.VideoInfoService;
import com.miniBili.utils.CopyTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.*;

@RestController
@RequestMapping("/uhome")
@Validated
public class UHomeController extends ABaseController{
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private VideoInfoService videoInfoService;
    @Autowired
    private UserFocusService userFocusService;
    @Autowired
    private UserActionService userActionService;

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    @RequestMapping("/getUserInfo")
    public ResponseVO getUserInfo(@NotEmpty String userId){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        UserInfo userInfo = userInfoService.getUserDetail(tokenInfoDto==null?null:tokenInfoDto.getUserId(),userId);
        UserVo userVo = CopyTools.copy(userInfo,UserVo.class);
        return getSuccessResponseVO(userVo);
    }

    /**
     * 修改用户信息
     * @param nickName
     * @param avatar
     * @param sex
     * @param birthday
     * @param school
     * @param personIntroduction
     * @param noticeInfo
     * @return
     */
    @RequestMapping("/updateUserInfo")
    public ResponseVO updateUserInfo(@NotEmpty @Size(max = 20) String nickName,
                                     @NotEmpty @Size(max = 100) String avatar,
                                     @NotNull Integer sex,
                                     @Size(max = 10) String birthday,
                                     @Size(max = 150) String school,
                                     @Size(max = 80) String personIntroduction,
                                     @Size(max = 300) String noticeInfo){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(tokenInfoDto.getUserId());
        userInfo.setNickName(nickName);
        userInfo.setAvatar(avatar);
        userInfo.setSex(sex);
        userInfo.setBirthday(birthday);
        userInfo.setSchool(school);
        userInfo.setPersonIntroduction(personIntroduction);
        userInfo.setNoticeInfo(noticeInfo);
        userInfoService.updateUserInfo(userInfo,tokenInfoDto);
        return getSuccessResponseVO(null);
    }


    @RequestMapping("/saveTheme")
    public ResponseVO savetheme(@Min(1) @Max(10) @NotNull Integer theme){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(tokenInfoDto.getUserId());
        userInfo.setTheme(theme);
        userInfoService.updateUserInfoByUserId(userInfo, userInfo.getUserId());
        return getSuccessResponseVO(null);
    }
}
