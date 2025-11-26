package com.miniBili.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 *  数据库操作接口
 */
public interface StatisticsInfoMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据StatisticsDateAndUserIdAndDataType更新
	 */
	 Integer updateByStatisticsDateAndUserIdAndDataType(@Param("bean") T t, @Param("statisticsDate") Date statisticsDate, @Param("userId") String userId, @Param("dataType") Integer dataType);


	/**
	 * 根据StatisticsDateAndUserIdAndDataType删除
	 */
	 Integer deleteByStatisticsDateAndUserIdAndDataType(@Param("statisticsDate") Date statisticsDate,@Param("userId") String userId,@Param("dataType") Integer dataType);


	/**
	 * 根据StatisticsDateAndUserIdAndDataType获取对象
	 */
	 T selectByStatisticsDateAndUserIdAndDataType(@Param("statisticsDate") Date statisticsDate,@Param("userId") String userId,@Param("dataType") Integer dataType);


}
