package com.miniBili.service;

import java.util.List;

import com.miniBili.entity.query.UserActionQuery;
import com.miniBili.entity.po.UserAction;
import com.miniBili.entity.vo.PaginationResultVO;


/**
 * 用户行为，点赞、评论 业务接口
 */
public interface UserActionService {

	/**
	 * 根据条件查询列表
	 */
	List<UserAction> findListByParam(UserActionQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(UserActionQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<UserAction> findListByPage(UserActionQuery param);

	/**
	 * 新增
	 */
	Integer add(UserAction bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserAction> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<UserAction> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(UserAction bean,UserActionQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(UserActionQuery param);

	/**
	 * 根据ActionId查询对象
	 */
	UserAction getUserActionByActionId(Integer actionId);


	/**
	 * 根据ActionId修改
	 */
	Integer updateUserActionByActionId(UserAction bean,Integer actionId);


	/**
	 * 根据ActionId删除
	 */
	Integer deleteUserActionByActionId(Integer actionId);


	/**
	 * 根据VideoIdAndCommentIdAndActionIdAndUserId查询对象
	 */
	UserAction getUserActionByVideoIdAndCommentIdAndActionIdAndUserId(String videoId,Integer commentId,Integer actionId,String userId);


	/**
	 * 根据VideoIdAndCommentIdAndActionIdAndUserId修改
	 */
	Integer updateUserActionByVideoIdAndCommentIdAndActionIdAndUserId(UserAction bean,String videoId,Integer commentId,Integer actionId,String userId);


	/**
	 * 根据VideoIdAndCommentIdAndActionIdAndUserId删除
	 */
	Integer deleteUserActionByVideoIdAndCommentIdAndActionIdAndUserId(String videoId,Integer commentId,Integer actionId,String userId);

}