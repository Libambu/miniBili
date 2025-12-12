package com.miniBili.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *  数据库操作接口
 */
@Mapper
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


	List<T> selectFans(String statisticsDate);

	List<T> selectComments(String statisticsDate);

	List<T> selectOthers(String statisticsDate);

	List<T> selectDanmu(String statisticsDate);

	Map<String, Integer> selectTotalCountInfo(@Param("userId") String userId);

	List<T> selectTotalListByParam(@Param("query") P statisticsInfoQuery);

	List<T> findUserCountTotalInfoByParam(@Param("query") P statisticsInfoQuery);
}
