package com.miniBili.mappers;

import org.apache.ibatis.annotations.Param;

/**
 *  数据库操作接口
 */
public interface UserVideoSeriesMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据SeriesId更新
	 */
	 Integer updateBySeriesId(@Param("bean") T t,@Param("seriesId") Integer seriesId);


	/**
	 * 根据SeriesId删除
	 */
	 Integer deleteBySeriesId(@Param("seriesId") Integer seriesId);


	/**
	 * 根据SeriesId获取对象
	 */
	 T selectBySeriesId(@Param("seriesId") Integer seriesId);


}
