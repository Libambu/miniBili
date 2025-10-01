package com.miniBili.admin.controller;

import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.enums.VideoStatusEnum;
import com.miniBili.entity.po.VideoInfoPost;
import com.miniBili.entity.query.VideoInfoPostQuery;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.service.VideoInfoFilePostService;
import com.miniBili.service.VideoInfoPostService;
import com.miniBili.service.VideoInfoService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@RequestMapping("admin/videoInfo")
public class VideoInfoController extends ABaseController {
    @Autowired
    private VideoInfoPostService videoInfoPostService;
    @Autowired
    private VideoInfoFilePostService videoInfoFilePostService;
    @Autowired
    private VideoInfoService videoInfoService;



    @RequestMapping("/loadVideoList")
    public ResponseVO loadVideoPost(VideoInfoPostQuery videoInfoPostQuery){
        videoInfoPostQuery.setOrderBy("v.last_update_time");
        videoInfoPostQuery.setQueryCountInfo(true);
        videoInfoPostQuery.setQueryUserInfo(true);
        PaginationResultVO resultVO = videoInfoPostService.findListByPage(videoInfoPostQuery);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/auditVideo")
    public ResponseVO auditVideo(@NotEmpty String videoId, @NotNull Integer status,String reason){
        videoInfoPostService.aduitVideo(videoId,status,reason);
        return getSuccessResponseVO(null);
    }
}
