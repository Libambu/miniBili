package com.miniBili.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.miniBili.entity.enums.PageSize;
import com.miniBili.entity.query.UserActionQuery;
import com.miniBili.entity.po.UserAction;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.query.SimplePage;
import com.miniBili.mappers.UserActionMapper;
import com.miniBili.service.UserActionService;
import com.miniBili.utils.StringTools;


/**
 * 用户行为，点赞、评论 业务接口实现
 */
@Service("userActionService")
public class UserActionServiceImpl implements UserActionService {

	@Resource
	private UserActionMapper<UserAction, UserActionQuery> userActionMapper;

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
		return this.userActionMapper.selectByVideoIdAndCommentIdAndActionIdAndUserId(videoId, commentId, actionId, userId);
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
}