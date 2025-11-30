package com.miniBili.web.controller;


import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.po.VideoPlayHistory;
import com.miniBili.entity.query.VideoPlayHistoryQuery;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.service.VideoPlayHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

@RequestMapping("/history")
@RestController
@Validated
@Slf4j
public class VideoFlayHistory extends ABaseController{

    @Autowired
    private VideoPlayHistoryService videoPlayHistoryService;



    @RequestMapping("loadHistory")
    public ResponseVO loadHistory(Integer pageNo){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        VideoPlayHistoryQuery videoPlayHistoryQuery = new VideoPlayHistoryQuery();
        videoPlayHistoryQuery.setUserId(tokenInfoDto.getUserId());
        videoPlayHistoryQuery.setOrderBy("last_update_time desc");
        videoPlayHistoryQuery.setPageNo(pageNo);
        videoPlayHistoryQuery.setQueryVideoDetail(true);
        return getSuccessResponseVO(videoPlayHistoryService.findListByPage(videoPlayHistoryQuery));
    }

    @RequestMapping("cleanHistory")
    public ResponseVO cleanHistory(){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        VideoPlayHistoryQuery videoPlayHistoryQuery = new VideoPlayHistoryQuery();
        videoPlayHistoryQuery.setUserId(tokenInfoDto.getUserId());
        videoPlayHistoryService.deleteByParam(videoPlayHistoryQuery);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("delHistory")
    public ResponseVO delHistory(@NotEmpty String  videoId){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        VideoPlayHistoryQuery videoPlayHistoryQuery = new VideoPlayHistoryQuery();
        videoPlayHistoryQuery.setUserId(tokenInfoDto.getUserId());
        videoPlayHistoryQuery.setVideoId(videoId);
        videoPlayHistoryService.deleteByParam(videoPlayHistoryQuery);
        return getSuccessResponseVO(null);
    }


}
