package com.miniBili.controller;


import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.query.VideoCommentQuery;
import com.miniBili.entity.query.VideoDanmuQuery;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.service.VideoCommentService;
import com.miniBili.service.impl.VideoDanmuServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/ucenter")
@Validated
@Slf4j
public class UcenterInteractionController extends ABaseController{


    @Autowired
    private VideoCommentService videoCommentService;
    @Autowired
    private VideoDanmuServiceImpl videoDanmuService;


    @RequestMapping("/loadComment")
    public ResponseVO loadComment(Integer pageNo,String videoId){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        VideoCommentQuery query = new VideoCommentQuery();
        query.setUserId(tokenInfoDto.getUserId());
        query.setVideoId(videoId);
        query.setOrderBy("comment_id desc");
        query.setPageNo(pageNo);
        query.setQueryVideoInfo(true);
        PaginationResultVO resultVO = videoCommentService.findListByPage(query);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/delComment")
    public ResponseVO deleteComment(@NotEmpty Integer commentId){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        videoCommentService.deleteVideoCommentByCommentId(commentId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadDanmu")
    public ResponseVO loadDanmu(Integer pageNo,String videoId){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        VideoDanmuQuery query = new VideoDanmuQuery();
        query.setVideoId(videoId);
        query.setVideoUserId(tokenInfoDto.getUserId());
        query.setOrderBy("danmu_id desc");
        query.setPageNo(pageNo);
        query.setQueryVideoInfo(true);
        PaginationResultVO resultVO = videoDanmuService.findListByPage(query);
        return getSuccessResponseVO(resultVO);
    }
}
