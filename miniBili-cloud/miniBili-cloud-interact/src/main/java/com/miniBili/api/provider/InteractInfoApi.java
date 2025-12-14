package com.miniBili.api.provider;

import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.query.VideoCommentQuery;
import com.miniBili.entity.query.VideoDanmuQuery;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.service.VideoCommentService;
import com.miniBili.service.VideoDanmuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Validated
@RequestMapping(Constants.Inner_api_prefix)
@Slf4j
public class InteractInfoApi {

    @Autowired
    private VideoCommentService videoCommentService;

    @Autowired
    private VideoDanmuService videoDanmuService;

    @RequestMapping("/interact/admin/loadComment")
    public PaginationResultVO loadComment(Integer pageNo, String videoNameFuzzy){
        VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
        videoCommentQuery.setOrderBy("comment_id desc");
        videoCommentQuery.setPageNo(pageNo);
        videoCommentQuery.setQueryVideoInfo(true);
        videoCommentQuery.setVideoNameFuzzy(videoNameFuzzy);
        //videoCommentQuery.setLoadChildren(true);
        PaginationResultVO resultVO = videoCommentService.findListByPage(videoCommentQuery);
        return resultVO;
    }

    @RequestMapping("/interact/admin/loadDanmu")
    public PaginationResultVO loadDanmu(Integer pageNo,String videoNameFuzzy){
        VideoDanmuQuery danmuQuery = new VideoDanmuQuery();
        danmuQuery.setOrderBy("danmu_id desc");
        danmuQuery.setPageNo(pageNo);
        danmuQuery.setQueryVideoInfo(true);
        danmuQuery.setVideoNameFuzzy(videoNameFuzzy);
        PaginationResultVO  resultVO = videoDanmuService.findListByPage(danmuQuery);
        return resultVO;
    }
}
