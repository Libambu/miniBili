package com.miniBili.web.controller;

import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.enums.UserActionTypeEnum;
import com.miniBili.entity.po.UserAction;
import com.miniBili.entity.po.VideoComment;
import com.miniBili.entity.po.VideoInfo;
import com.miniBili.entity.po.VideoInfoPost;
import com.miniBili.entity.query.UserActionQuery;
import com.miniBili.entity.query.VideoCommentQuery;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.entity.vo.VideoCommentResultVo;
import com.miniBili.service.UserActionService;
import com.miniBili.service.VideoCommentService;
import com.miniBili.service.VideoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comment")
@Validated
@Slf4j
public class VideoCommentController extends ABaseController{
    @Autowired
    private VideoCommentService videoCommentService;
    @Autowired
    private UserActionService userActionService;
    @Autowired
    private VideoInfoService videoInfoService;

    @RequestMapping("/postComment")
    public ResponseVO postComment(@NotEmpty String videoId,
                                  @NotEmpty @Size(max=500) String content,
                                  Integer replyCommentId,
                                  @Size(max = 50) String imgPath){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        VideoComment comment = new VideoComment();
        comment.setUserId(tokenInfoDto.getUserId());
        comment.setAvatar(tokenInfoDto.getAvatar());
        comment.setNickName(tokenInfoDto.getNickName());
        comment.setVideoId(videoId);
        comment.setContent(content);
        comment.setImgPath(imgPath);
        videoCommentService.postComment(comment,replyCommentId);
        return getSuccessResponseVO(comment);
    }

    /**
     *
     * @param videoId
     * @param pageNo
     * @param orderType 根据时间倒序还是热度排
     * @return
     */
    @RequestMapping("/loadComment")
    public ResponseVO loadComment(@NotEmpty String videoId,
                                  Integer pageNo,
                                  Integer orderType){
        VideoInfo videoInfo = videoInfoService.getVideoInfoByVideoId(videoId);
        if(videoInfo.getInteraction()!=null&&videoInfo.getInteraction().contains("0")){
            return getSuccessResponseVO(new ArrayList<>());
        }
        VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
        videoCommentQuery.setVideoId(videoId);
        videoCommentQuery.setPageNo(pageNo);
        videoCommentQuery.setPageSize(15);
        //是否加载子评论
        videoCommentQuery.setLoadChildren(true);
        //只查一级评论
        videoCommentQuery.setpCommentId(0);
        String orderBy = orderType==null||orderType==0?"top_type desc,like_count desc,comment_id desc":"top_type desc comment_id desc";
        videoCommentQuery.setOrderBy(orderBy);
        PaginationResultVO<VideoComment> resultVO = videoCommentService.findListByPage(videoCommentQuery);
        //userAction也返回,展示当前是否已点赞
        List<UserAction> userActionList = new ArrayList<>();
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        if(tokenInfoDto!=null){
            UserActionQuery query = new UserActionQuery();
            query.setVideoId(videoInfo.getVideoId());
            query.setUserId(tokenInfoDto.getUserId());
            query.setActionTypeArray(new Integer[]{UserActionTypeEnum.COMMENT_LIKE.getType(),UserActionTypeEnum.COMMENT_HATE.getType()});
            userActionList = userActionService.findListByParam(query);
        }
        VideoCommentResultVo result = new VideoCommentResultVo();
        result.setCommentData(resultVO);
        result.setUserActionList(userActionList);
        return getSuccessResponseVO(result);
    }


}
