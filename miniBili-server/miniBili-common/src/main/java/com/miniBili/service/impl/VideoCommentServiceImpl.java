package com.miniBili.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.miniBili.entity.enums.ResponseCodeEnum;
import com.miniBili.entity.enums.UserActionTypeEnum;
import com.miniBili.entity.po.UserInfo;
import com.miniBili.entity.po.VideoInfo;
import com.miniBili.entity.query.UserInfoQuery;
import com.miniBili.entity.query.VideoInfoQuery;
import com.miniBili.exception.BusinessException;
import com.miniBili.mappers.UserInfoMapper;
import com.miniBili.mappers.VideoInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniBili.entity.enums.PageSize;
import com.miniBili.entity.query.VideoCommentQuery;
import com.miniBili.entity.po.VideoComment;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.query.SimplePage;
import com.miniBili.mappers.VideoCommentMapper;
import com.miniBili.service.VideoCommentService;
import com.miniBili.utils.StringTools;


/**
 * 评论 业务接口实现
 */
@Service("videoCommentService")
public class VideoCommentServiceImpl implements VideoCommentService {

	@Resource
	private VideoCommentMapper<VideoComment, VideoCommentQuery> videoCommentMapper;

	@Autowired
	private VideoInfoMapper<VideoInfo, VideoInfoQuery> videoInfoMapper;

	@Autowired
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<VideoComment> findListByParam(VideoCommentQuery param) {
		if(param.getLoadChildren()!=null&&param.getLoadChildren()){
			return this.videoCommentMapper.selectListWithChildren(param);
		}
		return this.videoCommentMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(VideoCommentQuery param) {
		return this.videoCommentMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<VideoComment> findListByPage(VideoCommentQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<VideoComment> list = this.findListByParam(param);
		PaginationResultVO<VideoComment> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(VideoComment bean) {
		return this.videoCommentMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<VideoComment> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.videoCommentMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<VideoComment> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.videoCommentMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(VideoComment bean, VideoCommentQuery param) {
		StringTools.checkParam(param);
		return this.videoCommentMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(VideoCommentQuery param) {
		StringTools.checkParam(param);
		return this.videoCommentMapper.deleteByParam(param);
	}

	/**
	 * 根据CommentId获取对象
	 */
	@Override
	public VideoComment getVideoCommentByCommentId(Integer commentId) {
		return this.videoCommentMapper.selectByCommentId(commentId);
	}

	/**
	 * 根据CommentId修改
	 */
	@Override
	public Integer updateVideoCommentByCommentId(VideoComment bean, Integer commentId) {
		return this.videoCommentMapper.updateByCommentId(bean, commentId);
	}

	/**
	 * 根据CommentId删除
	 */
	@Override
	public Integer deleteVideoCommentByCommentId(Integer commentId) {
		return this.videoCommentMapper.deleteByCommentId(commentId);
	}

	@Override
	public void postComment(VideoComment comment, Integer replyCommentId) {
		//首先查一下video是否可以评论
		VideoInfo videoInfo = videoInfoMapper.selectByVideoId(comment.getVideoId());
		if(videoInfo==null){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if(videoInfo.getInteraction()!=null&&videoInfo.getInteraction().contains("0")){
			throw new BusinessException("UP主已关闭评论区");
		}
		//通过replyCommentId获取父级评论
		if(replyCommentId!=null){
			VideoComment replyComment = getVideoCommentByCommentId(replyCommentId);
			if(replyComment==null||!replyComment.getVideoId().equals(comment.getVideoId())){
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
			if(replyComment.getpCommentId()==0){
				//如果是一级评论
				comment.setpCommentId(replyComment.getCommentId());
			}else{
				//都是二级评论
				comment.setpCommentId(replyComment.getpCommentId());
				comment.setReplyUserId(replyComment.getUserId());
			}
			UserInfo userInfo = userInfoMapper.selectByUserId(comment.getUserId());
			comment.setReplyNickName(userInfo.getNickName());
			comment.setReplyAvatar(userInfo.getAvatar());
		}else{
			comment.setpCommentId(0);
		}
		comment.setPostTime(new Date());
		comment.setVideoId(videoInfo.getVideoId());
		comment.setVideoUserId(videoInfo.getUserId());
		videoCommentMapper.insert(comment);
		//下一步要增加评论数量,只算一级评论
		if(comment.getpCommentId()==0){
			videoInfoMapper.updateCountInfo(comment.getVideoId(), UserActionTypeEnum.VIDEO_COMMENT.getField(),1);
		}
	}

}