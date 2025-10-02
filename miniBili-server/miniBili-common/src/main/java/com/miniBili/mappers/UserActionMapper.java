package com.miniBili.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 用户行为，点赞、评论 数据库操作接口
 */
public interface UserActionMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据ActionId更新
	 */
	 Integer updateByActionId(@Param("bean") T t,@Param("actionId") Integer actionId);


	/**
	 * 根据ActionId删除
	 */
	 Integer deleteByActionId(@Param("actionId") Integer actionId);


	/**
	 * 根据ActionId获取对象
	 */
	 T selectByActionId(@Param("actionId") Integer actionId);


	/**
	 * 根据VideoIdAndCommentIdAndActionIdAndUserId更新
	 */
	 Integer updateByVideoIdAndCommentIdAndActionIdAndUserId(@Param("bean") T t,@Param("videoId") String videoId,@Param("commentId") Integer commentId,@Param("actionId") Integer actionId,@Param("userId") String userId);


	/**
	 * 根据VideoIdAndCommentIdAndActionIdAndUserId删除
	 */
	 Integer deleteByVideoIdAndCommentIdAndActionIdAndUserId(@Param("videoId") String videoId,@Param("commentId") Integer commentId,@Param("actionId") Integer actionId,@Param("userId") String userId);


	/**
	 * 根据VideoIdAndCommentIdAndActionIdAndUserId获取对象
	 */
	 T selectByVideoIdAndCommentIdAndActionIdAndUserId(@Param("videoId") String videoId,@Param("commentId") Integer commentId,@Param("actionId") Integer actionId,@Param("userId") String userId);


}
