package com.miniBili.controller;

import com.miniBili.annotation.RecordUserMessage;
import com.miniBili.api.consumer.WebClient;
import com.miniBili.entity.enums.MessageTypeEnum;
import com.miniBili.entity.query.VideoInfoPostQuery;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@RequestMapping("/videoInfo")
public class VideoInfoController extends ABaseController {


    @Autowired
    private WebClient webClient;



    @RequestMapping("/loadVideoList")
    public ResponseVO loadVideoPost(VideoInfoPostQuery videoInfoPostQuery){
        videoInfoPostQuery.setOrderBy("v.last_update_time");
        videoInfoPostQuery.setQueryCountInfo(true);
        videoInfoPostQuery.setQueryUserInfo(true);
        PaginationResultVO resultVO = webClient.findListByPage(videoInfoPostQuery);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/auditVideo")
    @RecordUserMessage(messageType = MessageTypeEnum.SYS)
    public ResponseVO auditVideo(@NotEmpty String videoId, @NotNull Integer status,String reason){
        webClient.aduitVideo(videoId,status,reason);
        return getSuccessResponseVO(null);
    }
    @RequestMapping("/recommendVideo")
    public ResponseVO recommendVideo(@NotEmpty String videoId){
        webClient.recommendVideo(videoId);
        return getSuccessResponseVO(null);
    }

    /**
     * 删除没写
     * @param videoId
     * @return
     */
    @RequestMapping("/deleteVideo")
    public ResponseVO deleteVideo(@NotEmpty String videoId){
        return getSuccessResponseVO(null);
    }
}
