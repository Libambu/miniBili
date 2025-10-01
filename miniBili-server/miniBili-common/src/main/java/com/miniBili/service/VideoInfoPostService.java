package com.miniBili.service;

import java.util.List;

import com.miniBili.entity.po.VideoInfoFilePost;
import com.miniBili.entity.query.VideoInfoPostQuery;
import com.miniBili.entity.po.VideoInfoPost;
import com.miniBili.entity.vo.PaginationResultVO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * 审核中的信息表 业务接口
 */
public interface VideoInfoPostService {

	/**
	 * 根据条件查询列表
	 */
	List<VideoInfoPost> findListByParam(VideoInfoPostQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(VideoInfoPostQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<VideoInfoPost> findListByPage(VideoInfoPostQuery param);

	/**
	 * 新增
	 */
	Integer add(VideoInfoPost bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<VideoInfoPost> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<VideoInfoPost> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(VideoInfoPost bean,VideoInfoPostQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(VideoInfoPostQuery param);

	/**
	 * 根据VideoId查询对象
	 */
	VideoInfoPost getVideoInfoPostByVideoId(String videoId);


	/**
	 * 根据VideoId修改
	 */
	Integer updateVideoInfoPostByVideoId(VideoInfoPost bean,String videoId);


	/**
	 * 根据VideoId删除
	 */
	Integer deleteVideoInfoPostByVideoId(String videoId);

    void saveVideoInfo(VideoInfoPost videoInfoPost, List<VideoInfoFilePost> filePostList);

	void transferVideoFile(VideoInfoFilePost videoInfoFilePost);

	void aduitVideo(@NotEmpty String videoId, @NotNull Integer status, String reason);
}