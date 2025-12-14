package com.miniBili.controller;


import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.po.VideoInfo;
import com.miniBili.entity.query.VideoInfoQuery;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.service.VideoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ucenter")
@Validated
@Slf4j
public class UcenterInteractionController extends ABaseController{

    @Autowired
    private VideoInfoService videoInfoService;


    @RequestMapping("/loadAllVideo")
    public ResponseVO loadAllVideo(){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setUserId(tokenInfoDto.getUserId());
        videoInfoQuery.setOrderBy("create_time desc");
        List<VideoInfo> videoInfoList = videoInfoService.findListByParam(videoInfoQuery);
        return getSuccessResponseVO(videoInfoList);
    }
}
