package com.miniBili.web.controller;

import com.miniBili.component.RedisComponent;
import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.enums.ResponseCodeEnum;
import com.miniBili.entity.enums.UserActionTypeEnum;
import com.miniBili.entity.enums.VideoRecommendTypeEnum;
import com.miniBili.entity.po.UserAction;
import com.miniBili.entity.po.VideoInfo;
import com.miniBili.entity.po.VideoInfoFile;
import com.miniBili.entity.po.VideoInfoPost;
import com.miniBili.entity.query.UserActionQuery;
import com.miniBili.entity.query.VideoInfoFileQuery;
import com.miniBili.entity.query.VideoInfoQuery;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.entity.vo.VideoInfoResultVO;
import com.miniBili.exception.BusinessException;
import com.miniBili.mappers.UserActionMapper;
import com.miniBili.service.UserActionService;
import com.miniBili.service.VideoInfoFileService;
import com.miniBili.service.VideoInfoService;
import com.oracle.js.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/video")
@Validated
public class VideoController extends ABaseController{
    @Autowired
    private VideoInfoService videoInfoService;
    @Autowired
    private VideoInfoFileService videoInfoFileService;
    @Autowired
    private UserActionService userActionService;
    @Autowired
    private RedisComponent redisComponent;

    /**
     * 获取推荐页
     * @return
     */
    @RequestMapping("/loadRecommendVideo")
    public ResponseVO  loadRecommendVideo(){
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setOrderBy("create_time desc");
        videoInfoQuery.setRecommendType(VideoRecommendTypeEnum.RECOMMEND.getType());
        videoInfoQuery.setQueryUserInfo(true);
        List<VideoInfo> recommendVideoList = videoInfoService.findListByParam(videoInfoQuery);
        return getSuccessResponseVO(recommendVideoList);
    }

    /**
     * 获取非推荐页
     * @param pCategoryId
     * @param categoryId
     * @param pageNo
     * @return
     */
    @RequestMapping("/loadVideo")
    public ResponseVO  loadVideo(Integer pCategoryId,Integer categoryId,Integer pageNo){
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setCategoryId(categoryId);
        videoInfoQuery.setpCategoryId(pCategoryId);
        videoInfoQuery.setPageNo(pageNo);
        videoInfoQuery.setQueryUserInfo(true);
        videoInfoQuery.setOrderBy("create_time desc");
        videoInfoQuery.setRecommendType(VideoRecommendTypeEnum.NO_RECOMMEND.getType());
        PaginationResultVO resultVO = videoInfoService.findListByPage(videoInfoQuery);
        return getSuccessResponseVO(resultVO);
    }

    /**
     * 获取视频的基础数据
     * @param videoId
     * @return
     */
    @RequestMapping("/getVideoInfo")
    public ResponseVO getVideoInfo(@NotEmpty String videoId){
        VideoInfo videoInfo = videoInfoService.getVideoInfoByVideoId(videoId);
        if(videoInfo == null){
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        // 获取用户行为：点赞，投币，收藏,要查看是否登录，登录了才有这个功能
        List<UserAction> userActions = new ArrayList<>();
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        if(tokenInfoDto!=null){
            UserActionQuery query = new UserActionQuery();
            query.setVideoId(videoId);
            query.setUserId(tokenInfoDto.getUserId());
            query.setActionTypeArray(new Integer[]{UserActionTypeEnum.VIDEO_LIKE.getType(),UserActionTypeEnum.VIDEO_COLLECT.getType(),UserActionTypeEnum.VIDEO_COIN.getType()});
            userActions = userActionService.findListByParam(query);
        }
        VideoInfoResultVO resultVO = new VideoInfoResultVO();
        resultVO.setVideoInfo(videoInfo,userActions);
        return getSuccessResponseVO(resultVO);
    }

    /**
     * 获取视频文件
     * @param videoId
     * @return
     */
    @RequestMapping("/loadVideoPList")
    public ResponseVO loadVideoPList(@NotEmpty String videoId){
        VideoInfoFileQuery query = new VideoInfoFileQuery();
        query.setVideoId(videoId);
        query.setOrderBy("file_index asc");
        List<VideoInfoFile> fileList = videoInfoFileService.findListByParam(query);
        return getSuccessResponseVO(fileList);
    }

    /**
     * 使用轮询获取在线人数
     * @param fileId
     * @param deviceId
     * @return
     */
    @RequestMapping("/reportVideoPlayOnline")
    public ResponseVO reportVideoPlayOnline(@NotEmpty String fileId,@NotEmpty String deviceId){
        return getSuccessResponseVO(redisComponent.reportVideoPlayOnline(fileId,deviceId));
    }
}
