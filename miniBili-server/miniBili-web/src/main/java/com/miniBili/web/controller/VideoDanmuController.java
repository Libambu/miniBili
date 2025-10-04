package com.miniBili.web.controller;

import com.miniBili.entity.enums.ResponseCodeEnum;
import com.miniBili.entity.po.VideoDanmu;
import com.miniBili.entity.po.VideoInfo;
import com.miniBili.entity.query.VideoDanmuQuery;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.exception.BusinessException;
import com.miniBili.service.VideoDanmuService;
import com.miniBili.service.impl.VideoInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;

@RequestMapping("/danmu")
@RestController
@Validated
public class VideoDanmuController extends ABaseController{

    @Autowired
    private VideoDanmuService videoDanmuService;
    @Autowired
    private VideoInfoServiceImpl videoInfoService;

    /**
     * 发布弹幕
     * @param videoId
     * @param fileId
     * @param text
     * @param mode
     * @param color
     * @param time
     * @return
     */
    @RequestMapping("/postDanmu")
    public ResponseVO postdanmu(@NotEmpty String videoId,
                                @NotEmpty String fileId,
                                @NotEmpty @Size(max = 30) String text,
                                @NotNull Integer mode,
                                @NotEmpty String color,
                                @NotNull Integer time){
        VideoDanmu videoDanmu = new VideoDanmu();
        videoDanmu.setVideoId(videoId);
        videoDanmu.setFileId(fileId);
        videoDanmu.setText(text);
        videoDanmu.setMode(mode);
        videoDanmu.setColor(color);
        videoDanmu.setTime(time);
        videoDanmu.setPostTime(new Date());
        videoDanmu.setUserId(getTokenInfoDto().getUserId());

        videoDanmuService.saveDanmu(videoDanmu);
        return getSuccessResponseVO(null);
    }


    @RequestMapping("/loadDanmu")
    public ResponseVO loadDanmu(@NotEmpty String fileId,@NotEmpty String videoId){
        VideoInfo videoInfo = videoInfoService.getVideoInfoByVideoId(videoId);
        if(videoInfo.getInteraction()!=null&&videoInfo.getInteraction().contains("1")){
            return getSuccessResponseVO(new ArrayList<>());
        }
        VideoDanmuQuery videoDanmuQuery = new VideoDanmuQuery();
        videoDanmuQuery.setFileId(fileId);
        videoDanmuQuery.setVideoId(videoId);
        videoDanmuQuery.setOrderBy("danmu_id asc");
        return getSuccessResponseVO(videoDanmuService.findListByParam(videoDanmuQuery));
    }



}
