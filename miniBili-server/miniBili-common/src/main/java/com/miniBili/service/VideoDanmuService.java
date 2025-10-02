package com.miniBili.service;

import java.util.List;

import com.miniBili.entity.query.VideoDanmuQuery;
import com.miniBili.entity.po.VideoDanmu;
import com.miniBili.entity.vo.PaginationResultVO;


/**
 * 视频弹幕 业务接口
 */
public interface VideoDanmuService {

	/**
	 * 根据条件查询列表
	 */
	List<VideoDanmu> findListByParam(VideoDanmuQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(VideoDanmuQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<VideoDanmu> findListByPage(VideoDanmuQuery param);

	/**
	 * 新增
	 */
	Integer add(VideoDanmu bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<VideoDanmu> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<VideoDanmu> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(VideoDanmu bean,VideoDanmuQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(VideoDanmuQuery param);

	/**
	 * 根据DanmuId查询对象
	 */
	VideoDanmu getVideoDanmuByDanmuId(Integer danmuId);


	/**
	 * 根据DanmuId修改
	 */
	Integer updateVideoDanmuByDanmuId(VideoDanmu bean,Integer danmuId);


	/**
	 * 根据DanmuId删除
	 */
	Integer deleteVideoDanmuByDanmuId(Integer danmuId);

}