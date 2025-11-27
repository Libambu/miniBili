package com.miniBili.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.miniBili.entity.dto.UserMessageCount;
import com.miniBili.entity.dto.UserMessageExtendDto;
import com.miniBili.entity.enums.MessageTypeEnum;
import com.miniBili.entity.po.VideoComment;
import com.miniBili.entity.po.VideoInfo;
import com.miniBili.entity.po.VideoInfoPost;
import com.miniBili.entity.query.VideoInfoQuery;
import com.miniBili.mappers.VideoCommentMapper;
import com.miniBili.mappers.VideoInfoMapper;
import com.miniBili.mappers.VideoInfoPostMapper;
import com.miniBili.service.VideoInfoPostService;
import com.miniBili.utils.JsonUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.miniBili.entity.enums.PageSize;
import com.miniBili.entity.query.UserMessageQuery;
import com.miniBili.entity.po.UserMessage;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.query.SimplePage;
import com.miniBili.mappers.UserMessageMapper;
import com.miniBili.service.UserMessageService;
import com.miniBili.utils.StringTools;


/**
 * 用户消息表 业务接口实现
 */
@Service("userMessageService")
public class UserMessageServiceImpl implements UserMessageService {

	@Resource
	private UserMessageMapper<UserMessage, UserMessageQuery> userMessageMapper;
	@Autowired
	private VideoInfoMapper<VideoInfo, VideoInfoQuery> videoInfoMapper;
    @Autowired
    private VideoCommentMapper videoCommentMapper;
    @Autowired
    private VideoInfoPostServiceImpl videoInfoPostService;
    @Autowired
    private VideoInfoPostMapper videoInfoPostMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserMessage> findListByParam(UserMessageQuery param) {
		return this.userMessageMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(UserMessageQuery param) {
		return this.userMessageMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<UserMessage> findListByPage(UserMessageQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<UserMessage> list = this.findListByParam(param);
		PaginationResultVO<UserMessage> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(UserMessage bean) {
		return this.userMessageMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserMessage> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userMessageMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserMessage> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userMessageMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(UserMessage bean, UserMessageQuery param) {
		StringTools.checkParam(param);
		return this.userMessageMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(UserMessageQuery param) {
		StringTools.checkParam(param);
		return this.userMessageMapper.deleteByParam(param);
	}

	/**
	 * 根据MessageId获取对象
	 */
	@Override
	public UserMessage getUserMessageByMessageId(Integer messageId) {
		return this.userMessageMapper.selectByMessageId(messageId);
	}

	/**
	 * 根据MessageId修改
	 */
	@Override
	public Integer updateUserMessageByMessageId(UserMessage bean, Integer messageId) {
		return this.userMessageMapper.updateByMessageId(bean, messageId);
	}

	/**
	 * 根据MessageId删除
	 */
	@Override
	public Integer deleteUserMessageByMessageId(Integer messageId) {
		return this.userMessageMapper.deleteByMessageId(messageId);
	}

	@Override
	@Async
	public void saveUserMessage(String videoId, String sendUserId, MessageTypeEnum messageTypeEnum, String content, Integer replyCommentId) {
		VideoInfo videoInfo = videoInfoMapper.selectByVideoId(videoId);
		if(videoInfo==null){
			return;
		}
		UserMessageExtendDto userMessageExtendDto = new UserMessageExtendDto();
		userMessageExtendDto.setMessageContent(content);
		String userId = videoInfo.getUserId();
		//收藏点赞已经记录的不在记录
		if(ArrayUtils.contains(new Integer[] {MessageTypeEnum.LIKE.getType(),MessageTypeEnum.COLLECTION.getType()},messageTypeEnum.getType())){
			//查消息表，如果有就不再通知
		}
		//然后将信息写入
		UserMessage userMessage = new UserMessage();
		userMessage.setUserId(userId);
		userMessage.setVideoId(videoId);
		userMessage.setReadType(0);
		userMessage.setCreateTime(new Date());
		userMessage.setMessageType(messageTypeEnum.getType());
		userMessage.setSendUserId(sendUserId);
		//评论特殊处理
		if(replyCommentId!=null){
			//查出上一级评论
			VideoComment o = (VideoComment)videoCommentMapper.selectByCommentId(replyCommentId);
			if(o!=null){
				userId = o.getUserId();
				userMessageExtendDto.setMessageContentReply(o.getContent());
			}
		}
		//自己给自己回复消息就不用发
		if(userId.equals(sendUserId)){
			return;
		}
		//系统消息也要特殊处理
		if(messageTypeEnum.SYS==messageTypeEnum){
			VideoInfoPost videoInfoPost = (VideoInfoPost)videoInfoPostMapper.selectByVideoId(videoId);
			userMessageExtendDto.setAuditStatus(videoInfoPost.getStatus());
		}
		userMessage.setExtendJson(JsonUtils.converObj2Json(userMessageExtendDto));
		userMessageMapper.insert(userMessage);;
	}

	@Override
	public List<UserMessageCount> getMessageTypeNoReadCount(String userId) {
		return userMessageMapper.getMessageTypeNoReadCount(userId);
	}
}