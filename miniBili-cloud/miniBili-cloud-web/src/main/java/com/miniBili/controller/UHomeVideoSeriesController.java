package com.miniBili.controller;


import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.enums.ResponseCodeEnum;
import com.miniBili.entity.po.UserVideoSeries;
import com.miniBili.entity.po.UserVideoSeriesVideo;
import com.miniBili.entity.po.VideoInfo;
import com.miniBili.entity.query.UserVideoSeriesQuery;
import com.miniBili.entity.query.UserVideoSeriesVideoQuery;
import com.miniBili.entity.query.VideoInfoQuery;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.exception.BusinessException;
import com.miniBili.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("uhome/series")
@Validated
public class UHomeVideoSeriesController extends ABaseController{
    @Autowired
    private VideoInfoService videoInfoService;
    @Autowired
    private UserVideoSeriesService userVideoSeriesService;
    @Autowired
    private UserVideoSeriesVideoService userVideoSeriesVideoService;
    @Autowired
    private VideoInfoPostService videoInfoPostService;
    @Autowired
    private VideoInfoFilePostService videoInfoFilePostService;

    @RequestMapping("/loadVideoSeries")
    public ResponseVO loadVideoSeries(@NotEmpty String userId){
        List<UserVideoSeries> videoSeries = userVideoSeriesService.getUserAllSeries(userId);
        return getSuccessResponseVO(videoSeries);
    }

    /**
     * 保存分类信息
     * @param seriesId null为新增 否则为修改
     * @param seriesName
     * @param seriesDescription
     * @param videoIds
     * @return
     */
    @RequestMapping("/saveVideoSeries")
    public ResponseVO saveVideoSeries(Integer seriesId,
                                      @NotEmpty @Size(max = 100) String seriesName,
                                      @Size(max = 200) String seriesDescription,
                                      String videoIds){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        UserVideoSeries videoSeries = new UserVideoSeries();
        videoSeries.setUserId(tokenInfoDto.getUserId());
        videoSeries.setSeriesId(seriesId);
        videoSeries.setSeriesName(seriesName);
        videoSeries.setSeriesDescription(seriesDescription);
        userVideoSeriesService.saveUserSeries(videoSeries,videoIds);
        return getSuccessResponseVO(videoSeries);
    }

    /**
     * 加载视频
     * @param seriesId seriesId里面有的视频就就不会在加载
     * @return
     */
    @RequestMapping("/loadAllVideo")
    public ResponseVO loadAllVideo(Integer seriesId){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        if(seriesId!=null){
            UserVideoSeriesVideoQuery userVideoSeriesVideoQuery = new UserVideoSeriesVideoQuery();
            userVideoSeriesVideoQuery.setSeriesId(seriesId);
            userVideoSeriesVideoQuery.setUserId(tokenInfoDto.getUserId());
            List<UserVideoSeriesVideo> seriesVideoList = userVideoSeriesVideoService.findListByParam(userVideoSeriesVideoQuery);
            //过滤掉当前视频分类里已经有的视频
            List<String> videoIds = seriesVideoList.stream().map(item->item.getVideoId()).collect(Collectors.toList());
            videoInfoQuery.setExcuteVideoId(videoIds);
        }
        videoInfoQuery.setUserId(tokenInfoDto.getUserId());
        List<VideoInfo> listByParam = videoInfoService.findListByParam(videoInfoQuery);
        return getSuccessResponseVO(listByParam);
    }

    @RequestMapping("/getVideoSeriesDetail")
    public ResponseVO getVideoSeriesDetail(@NotNull Integer seriesId){
        UserVideoSeries userVideoSeries = userVideoSeriesService.getUserVideoSeriesBySeriesId(seriesId);
        if(userVideoSeries==null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        UserVideoSeriesVideoQuery videoSeriesVideoQuery = new UserVideoSeriesVideoQuery();
        videoSeriesVideoQuery.setQueryVideoInfo(true);
        videoSeriesVideoQuery.setSeriesId(seriesId);
        videoSeriesVideoQuery.setOrderBy("sort asc");
        List<UserVideoSeriesVideo> seriesVideos = userVideoSeriesVideoService.findListByParam(videoSeriesVideoQuery);
        Map<String,Object>resultMap = new HashMap<>();
        resultMap.put("videoSeries",userVideoSeries);
        resultMap.put("seriesVideoList",seriesVideos);
        return getSuccessResponseVO(resultMap);
    }

    @RequestMapping("/saveSeriesVideo")
    public ResponseVO saveSeriesVideo(@NotNull Integer seriesId,@NotEmpty String videoIds){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        userVideoSeriesService.saveSeriesVideo(tokenInfoDto.getUserId(),seriesId,videoIds);
        return getSuccessResponseVO(null);
    }

    /**
     * 删除分类
     * @param seriesId
     * @return
     */
    @RequestMapping("/delVideoSeries")
    public ResponseVO delVideoSeries(@NotNull Integer seriesId){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        userVideoSeriesService.delVideoSeries(tokenInfoDto.getUserId(),seriesId);
        return getSuccessResponseVO(null);
    }


    /**
     * 删除分类下的一个视频
     * @param seriesId
     * @param videoId
     * @return
     */
    @RequestMapping("/delSeriesVideo")
    public ResponseVO delSeriesVideo(@NotNull Integer seriesId,@NotEmpty String videoId){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        userVideoSeriesService.delSeriesVideo(tokenInfoDto.getUserId(),seriesId,videoId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/changeVideoSeriesSort")
    public ResponseVO changeVideoSeriesSort(@NotEmpty String seriesIds){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        userVideoSeriesService.changeVideoSeriesSort(tokenInfoDto.getUserId(),seriesIds);
        return getSuccessResponseVO(null);
    }
    
    @RequestMapping("/loadVideoSeriesWithVideo")
    public ResponseVO loadVideoSeriesWithVideo(@NotEmpty String userId){
        UserVideoSeriesQuery seriesQuery = new UserVideoSeriesQuery();
        seriesQuery.setUserId(userId);
        seriesQuery.setOrderBy("sort asc");
        List<UserVideoSeries> videoSeries = userVideoSeriesService.findListWithVideoList(seriesQuery);
        return getSuccessResponseVO(videoSeries);
    }



}
