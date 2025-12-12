package com.miniBili.api.provider;


import com.miniBili.component.ESsearchComponent;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.po.VideoInfo;
import com.miniBili.entity.po.VideoInfoFile;
import com.miniBili.entity.po.VideoInfoPost;
import com.miniBili.mappers.VideoInfoMapper;
import com.miniBili.service.VideoInfoPostService;
import com.miniBili.service.VideoInfoService;
import com.miniBili.service.impl.VideoInfoFileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

@RestController
@Validated
public class VideoInfoApi {

    @Autowired
    private VideoInfoFileServiceImpl videoInfoFileService;
    @Autowired
    private VideoInfoService videoInfoService;
    @Autowired
    private ESsearchComponent eSsearchComponent;
    @Autowired
    private VideoInfoMapper videoInfoMapper;
    @Autowired
    private VideoInfoPostService videoInfoPostService;

    @RequestMapping(Constants.Inner_api_prefix +"/video/getVideoInfoByVideoId")
    public VideoInfo getVideoInfoByVideoId(@NotEmpty String videoId){
        return  videoInfoService.getVideoInfoByVideoId(videoId);
    }

    @RequestMapping(Constants.Inner_api_prefix +"/video/getVideoInfoFileByFileId")
    public VideoInfoFile getVideoInfoFileByFileId(@NotEmpty String FileId){
        return  videoInfoFileService.getVideoInfoFileByFileId(FileId);
    }

    @RequestMapping(Constants.Inner_api_prefix + "/video/updateCountInfo")
    void updateCountInfo(@RequestParam String videoId,@RequestParam String fileId,@RequestParam Integer changeCount){
        videoInfoMapper.updateCountInfo(videoId,fileId,changeCount);
    }

    @RequestMapping(Constants.Inner_api_prefix + "/video/getVideoInfoPostByVideoId")
    VideoInfoPost getVideoInfoPostByVideoId(@RequestParam String videoId){
       return  videoInfoPostService.getVideoInfoPostByVideoId(videoId);
    }

    @RequestMapping(Constants.Inner_api_prefix + "/video/updateDocCount")
    void updateDocCount(@RequestParam String videoId, @RequestParam String fieldName,@RequestParam Integer changeCount){
        eSsearchComponent.updateDocCount(videoId,fieldName,changeCount);
    }


}
