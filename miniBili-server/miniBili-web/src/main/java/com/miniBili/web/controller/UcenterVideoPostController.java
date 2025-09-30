package com.miniBili.web.controller;

import com.miniBili.component.RedisComponent;
import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.enums.VideoStatusEnum;
import com.miniBili.entity.po.VideoInfoFilePost;
import com.miniBili.entity.po.VideoInfoPost;
import com.miniBili.entity.query.VideoInfoPostQuery;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.entity.vo.VideoStatusCountInfo;
import com.miniBili.service.VideoInfoFilePostService;
import com.miniBili.service.VideoInfoPostService;
import com.miniBili.service.VideoInfoService;
import com.miniBili.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/ucenter")
@Slf4j
public class UcenterVideoPostController extends ABaseController {
    @Autowired
    private VideoInfoPostService videoInfoPostService;
    @Autowired
    private VideoInfoFilePostService videoInfoFilePostService;
    @Autowired
    private VideoInfoService videoInfoService;
    @Autowired
    private RedisComponent redisComponent;

    /**
     * 正式上传视频
     * @param videoId
     * @param videoCover
     * @param videoName
     * @param pCategoryId
     * @param categoryId
     * @param postType
     * @param tags
     * @param introduction
     * @param interaction
     * @param uploadFileList
     * @return
     */
    @RequestMapping("/postVideo")
    public ResponseVO postVideo(String videoId,
                                @NotEmpty String videoCover,
                                @NotEmpty @Size(max=100) String videoName,
                                @NotNull Integer pCategoryId,
                                Integer categoryId,
                                @NotNull Integer postType,
                                @NotEmpty @Size(max = 300) String tags,
                                @Size(max = 2000) String introduction,
                                @Size(max = 3) String interaction,
                                @Size(max = 3) String uploadFileList){

        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        List<VideoInfoFilePost> filePostList = JsonUtils.converJsonArray2List(uploadFileList,VideoInfoFilePost.class);
        VideoInfoPost videoInfoPost = new VideoInfoPost();
        videoInfoPost.setVideoId(videoId);
        videoInfoPost.setVideoCover(videoCover);
        videoInfoPost.setVideoName(videoName);
        videoInfoPost.setpCategoryId(pCategoryId);
        videoInfoPost.setCategoryId(categoryId);
        videoInfoPost.setPostType(postType);
        videoInfoPost.setTags(tags);
        videoInfoPost.setIntroduction(introduction);
        videoInfoPost.setInteraction(interaction);
        videoInfoPost.setUserId(tokenInfoDto.getUserId());

        videoInfoPostService.saveVideoInfo(videoInfoPost,filePostList);
        return getSuccessResponseVO(null);
    }

    /**
     * 列出视频列表
     * @param status
     * @param pageNo
     * @param videoNameFuzzy
     * @return
     */
    @RequestMapping("/loadVideoList")
    public ResponseVO loadVideoPost(Integer status,Integer pageNo,String videoNameFuzzy){
        TokenInfoDto dto = getTokenInfoDto();
        VideoInfoPostQuery query = new VideoInfoPostQuery();
        query.setUserId(dto.getUserId());
        query.setOrderBy("v.create_time desc");
        query.setPageNo(pageNo);
        if(status!=null){
            if(status==-1){
                query.setExcludeStatusArray(new Integer[]{VideoStatusEnum.STATUS3.getStatus(),VideoStatusEnum.STATUS4.getStatus()});
            }else{
                query.setStatus(status);
            }
        }
        query.setVideoNameFuzzy(videoNameFuzzy);
        query.setQueryCountInfo(true);
        PaginationResultVO resultVO = videoInfoPostService.findListByPage(query);
        return  getSuccessResponseVO(resultVO);
    }

    /**
     * 获取视频个数
     * @return
     */
    @RequestMapping("/getVideoCountInfo")
    public ResponseVO getVideoCountInfo(){
        TokenInfoDto dto = getTokenInfoDto();
        VideoInfoPostQuery query = new VideoInfoPostQuery();
        query.setUserId(dto.getUserId());
        query.setStatus(VideoStatusEnum.STATUS3.getStatus());
        Integer auditpass = videoInfoPostService.findCountByParam(query);
        query.setStatus(VideoStatusEnum.STATUS4.getStatus());
        Integer auditfail = videoInfoPostService.findCountByParam(query);

        query.setStatus(null);
        query.setExcludeStatusArray(new Integer[]{VideoStatusEnum.STATUS3.getStatus(),VideoStatusEnum.STATUS4.getStatus()});
        Integer inProgress = videoInfoPostService.findCountByParam(query);

        VideoStatusCountInfo videoStatusCountInfo = new VideoStatusCountInfo();
        if(auditfail==null){
            auditfail = 0;
        }
        if(inProgress==null){
            inProgress=0;
        }
        if(auditpass==null){
            auditpass=0;
        }
        videoStatusCountInfo.setAuditPassCount(auditpass);
        videoStatusCountInfo.setAuditFailCount(auditfail);
        videoStatusCountInfo.setInProgress(inProgress);
        return getSuccessResponseVO(videoStatusCountInfo);
    }


}
