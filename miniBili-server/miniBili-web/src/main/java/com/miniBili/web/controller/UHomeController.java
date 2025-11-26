package com.miniBili.web.controller;

import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.enums.ResponseCodeEnum;
import com.miniBili.entity.enums.UserActionTypeEnum;
import com.miniBili.entity.enums.VideoOrderType;
import com.miniBili.entity.po.UserInfo;
import com.miniBili.entity.query.UserActionQuery;
import com.miniBili.entity.query.UserFocusQuery;
import com.miniBili.entity.query.VideoInfoQuery;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.entity.vo.UserVo;
import com.miniBili.exception.BusinessException;
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


    /**
     * 切换主题
     * @param theme
     * @return
     */
    @RequestMapping("/saveTheme")
    public ResponseVO savetheme(@Min(1) @Max(10) @NotNull Integer theme){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(tokenInfoDto.getUserId());
        userInfo.setTheme(theme);
        userInfoService.updateUserInfoByUserId(userInfo, userInfo.getUserId());
        return getSuccessResponseVO(null);
    }

    /**
     * 关注
     * @param focusUserId
     * @return
     */
    @RequestMapping("/focus")
    public ResponseVO  focus(@NotEmpty String focusUserId){
        userFocusService.focusUser(getTokenInfoDto().getUserId(),focusUserId);
        return getSuccessResponseVO(null);
    }

    /**
     * 取消关注
     * @param focusUserId
     * @return
     */
    @RequestMapping("/cancelFocus")
    public ResponseVO  cancelFocus(@NotEmpty String focusUserId){
        userFocusService.cancleFocus(getTokenInfoDto().getUserId(),focusUserId);
        return getSuccessResponseVO(null);
    }

    /**
     * 加载关注列表
     * @param PageNo
     * @return
     */
    @RequestMapping("/loadFocusList")
    public ResponseVO getFocusList(Integer PageNo){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        UserFocusQuery focusQuery = new UserFocusQuery();
        focusQuery.setUserId(tokenInfoDto.getUserId());
        focusQuery.setOrderBy("focus_time desc");
        focusQuery.setQueryType(0);
        PaginationResultVO resultVO = userFocusService.findListByPage(focusQuery);
        return getSuccessResponseVO(resultVO);
    }

    /**
     * 加载粉丝列表
     * @param PageNo
     * @return
     */
    @RequestMapping("/loadFansList")
    public ResponseVO loadFansList(Integer PageNo){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        UserFocusQuery focusQuery = new UserFocusQuery();
        focusQuery.setFocusUserId(tokenInfoDto.getUserId());
        focusQuery.setOrderBy("focus_time desc");
        focusQuery.setQueryType(1);
        PaginationResultVO resultVO = userFocusService.findListByPage(focusQuery);
        return getSuccessResponseVO(resultVO);
    }

    /**
     * 加载主页视频
     * @param userId
     * @param type
     * @param pageNo
     * @param videoName
     * @param orderType
     * @return
     */
    @RequestMapping("/loadVideoList")
    public ResponseVO loadVideoList(String userId,Integer type,Integer pageNo,String videoName,Integer orderType){
        VideoInfoQuery infoQuery = new VideoInfoQuery();
        if(type!=null){
            infoQuery.setPageSize(10);
        }
        if(orderType!=null){
            VideoOrderType videoOrderType = VideoOrderType.getByType(orderType);
            if(videoOrderType==null){
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            infoQuery.setOrderBy(videoOrderType.getField() + " desc");
        }
        infoQuery.setVideoNameFuzzy(videoName);
        infoQuery.setPageNo(pageNo);
        infoQuery.setUserId(userId);
        PaginationResultVO resultVO = videoInfoService.findListByPage(infoQuery);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/loadUserCollection")
    public ResponseVO loadUserCollection(@NotEmpty String userId,Integer pageNo){
        UserActionQuery actionQuery = new UserActionQuery();
        actionQuery.setActionType(UserActionTypeEnum.VIDEO_COLLECT.getType());
        actionQuery.setUserId(userId);
        actionQuery.setPageNo(pageNo);
        actionQuery.setOrderBy("action_time desc");
        actionQuery.setQueryVideoInfo(true);
        PaginationResultVO resultVO = userActionService.findListByPage(actionQuery);
        return getSuccessResponseVO(resultVO);
    }

}
