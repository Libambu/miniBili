package com.miniBili.api.provider;


import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.po.VideoInfo;
import com.miniBili.entity.po.VideoInfoFile;
import com.miniBili.service.VideoInfoService;
import com.miniBili.service.impl.VideoInfoFileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping(Constants.Inner_api_prefix+"/video")
@Validated
public class VideoInfoApi {

    @Autowired
    private VideoInfoFileServiceImpl videoInfoFileService;
    @Autowired
    private VideoInfoService videoInfoService;

    @RequestMapping("getVideoInfoByVideoId")
    public VideoInfo getVideoInfoByVideoId(@NotEmpty String videoId){
        return  videoInfoService.getVideoInfoByVideoId(videoId);
    }

    @RequestMapping("getVideoInfoFileByFileId")
    public VideoInfoFile getVideoInfoFileByFileId(@NotEmpty String FileId){
        return  videoInfoFileService.getVideoInfoFileByFileId(FileId);
    }
}
