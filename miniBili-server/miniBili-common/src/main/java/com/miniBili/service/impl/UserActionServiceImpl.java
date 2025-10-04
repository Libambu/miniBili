package com.miniBili.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.miniBili.entity.enums.ResponseCodeEnum;
import com.miniBili.entity.enums.UserActionTypeEnum;
import com.miniBili.entity.po.VideoComment;
import com.miniBili.entity.po.VideoInfo;
import com.miniBili.entity.query.VideoCommentQuery;
import com.miniBili.entity.query.VideoInfoQuery;
import com.miniBili.exception.BusinessException;
import com.miniBili.mappers.UserInfoMapper;
import com.miniBili.mappers.VideoCommentMapper;
import com.miniBili.mappers.VideoInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniBili.entity.enums.PageSize;
import com.miniBili.entity.query.UserActionQuery;
import com.miniBili.entity.po.UserAction;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.query.SimplePage;
import com.miniBili.mappers.UserActionMapper;
import com.miniBili.service.UserActionService;
import com.miniBili.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;


/**
 * 用户行为，点赞、评论 业务接口实现
 */
@Slf4j
@Service("userActionService")
public class UserActionServiceImpl implements UserActionService {

	@Resource
	private UserActionMapper<UserAction, UserActionQuery> userActionMapper;

	@Autowired
	private VideoInfoMapper<VideoInfo, VideoInfoQuery> videoInfoMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
	@Autowired
	private VideoCommentMapper<VideoComment, VideoCommentQuery> videoCommentMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserAction> findListByParam(UserActionQuery param) {
		return this.userActionMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(UserActionQuery param) {
		return this.userActionMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<UserAction> findListByPage(UserActionQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<UserAction> list = this.findListByParam(param);
		PaginationResultVO<UserAction> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(UserAction bean) {
		return this.userActionMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserAction> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userActionMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserAction> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userActionMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(UserAction bean, UserActionQuery param) {
		StringTools.checkParam(param);
		return this.userActionMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(UserActionQuery param) {
		StringTools.checkParam(param);
		return this.userActionMapper.deleteByParam(param);
	}

	/**
	 * 根据ActionId获取对象
	 */
	@Override
	public UserAction getUserActionByActionId(Integer actionId) {
		return this.userActionMapper.selectByActionId(actionId);
	}

	/**
	 * 根据ActionId修改
	 */
	@Override
	public Integer updateUserActionByActionId(UserAction bean, Integer actionId) {
		return this.userActionMapper.updateByActionId(bean, actionId);
	}

	/**
	 * 根据ActionId删除
	 */
	@Override
	public Integer deleteUserActionByActionId(Integer actionId) {
		return this.userActionMapper.deleteByActionId(actionId);
	}

	/**
	 * 根据VideoIdAndCommentIdAndActionIdAndUserId获取对象
	 */
	@Override
	public UserAction getUserActionByVideoIdAndCommentIdAndActionIdAndUserId(String videoId, Integer commentId, Integer actionId, String userId) {
		return null;
	}

	/**
	 * 根据VideoIdAndCommentIdAndActionIdAndUserId修改
	 */
	@Override
	public Integer updateUserActionByVideoIdAndCommentIdAndActionIdAndUserId(UserAction bean, String videoId, Integer commentId, Integer actionId, String userId) {
		return this.userActionMapper.updateByVideoIdAndCommentIdAndActionIdAndUserId(bean, videoId, commentId, actionId, userId);
	}

	/**
	 * 根据VideoIdAndCommentIdAndActionIdAndUserId删除
	 */
	@Override
	public Integer deleteUserActionByVideoIdAndCommentIdAndActionIdAndUserId(String videoId, Integer commentId, Integer actionId, String userId) {
		return this.userActionMapper.deleteByVideoIdAndCommentIdAndActionIdAndUserId(videoId, commentId, actionId, userId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveAction(UserAction userAction) {
		VideoInfo videoInfo = videoInfoMapper.selectByVideoId(userAction.getVideoId());
		if(videoInfo==null){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		userAction.setVideoUserId(videoInfo.getUserId());
		UserActionTypeEnum userActionTypeEnum = UserActionTypeEnum.getByType(userAction.getActionType());
		//查询数据库里的信息
		UserAction dbAction =  userActionMapper.selectByVideoIdAndCommentIdAndActionTypeAndUserId(userAction.getVideoId(),userAction.getCommentId(),userAction.getActionType(),userAction.getUserId());
		userAction.setActionTime(new Date());
		//根据不同的操作类型进行switch
		switch (userActionTypeEnum){
			case VIDEO_LIKE:
			case VIDEO_COLLECT:
				if(dbAction!=null){
					userActionMapper.deleteByActionId(dbAction.getActionId());
				}else{
					userActionMapper.insert(userAction);
				}
				Integer changeCount = dbAction==null?1:-1;
				videoInfoMapper.updateCountInfo(userAction.getVideoId(), userActionTypeEnum.getField(),changeCount);
				//TODO更新es的点赞收藏的数量
				break;
			case VIDEO_COIN:
				if(videoInfo.getUserId().equals(userAction.getUserId())){
					throw new BusinessException("up主不能给自己投币");
				}
				if(dbAction!=null){
					throw new BusinessException("已经投过硬币了");
				}
				//先减少自己的硬币
				Integer updateCount = userInfoMapper.updateCoinCountInfo(userAction.getUserId(),-userAction.getActionCount());
				if(updateCount==0){
					throw new BusinessException("硬币不足");
				}
				//给up主加硬币
				updateCount = userInfoMapper.updateCoinCountInfo(videoInfo.getUserId(),userAction.getActionCount());
				if(updateCount==0){
					throw new BusinessException("投币失败");
				}
				//更改用户行为和视频信息
				userActionMapper.insert(userAction);
				videoInfoMapper.updateCountInfo(userAction.getVideoId(), userActionTypeEnum.getField(),userAction.getActionCount());
      			break;
			case COMMENT_LIKE:
			case COMMENT_HATE:
				UserActionTypeEnum opposeTyEnum  = userActionTypeEnum==UserActionTypeEnum.COMMENT_LIKE?UserActionTypeEnum.COMMENT_HATE:UserActionTypeEnum.COMMENT_LIKE;
				UserAction opposeAction = userActionMapper.selectByVideoIdAndCommentIdAndActionTypeAndUserId(userAction.getVideoId(),userAction.getCommentId(), opposeTyEnum.getType(),userAction.getUserId());
				if(opposeAction!=null){
					userActionMapper.deleteByActionId(opposeAction.getActionId());
				}
				if(dbAction!=null){
					userActionMapper.deleteByActionId(dbAction.getActionId());
				}else {
					userActionMapper.insert(userAction);
				}
				changeCount = dbAction==null?1:-1;
				Integer opposChangeCount =-changeCount;
				videoCommentMapper.updateCountInfo(userAction.getCommentId(),userActionTypeEnum.getField(),changeCount,
						opposeAction!=null? opposeTyEnum.getField():null,opposChangeCount);
				break;
		}

	}
}