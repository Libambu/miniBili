package com.miniBili.mappers;

import com.miniBili.entity.dto.CountInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 视频信息表(审核后) 数据库操作接口
 */
@Mapper
public interface VideoInfoMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据VideoId更新
	 */
	 Integer updateByVideoId(@Param("bean") T t,@Param("videoId") String videoId);


	/**
	 * 根据VideoId删除
	 */
	 Integer deleteByVideoId(@Param("videoId") String videoId);


	/**
	 * 根据VideoId获取对象
	 */
	 T selectByVideoId(@Param("videoId") String videoId);


	/**
	 * 更新数量
	 * @param videoId
	 * @param field 为字段名字
	 * @param changeCount
	 */
    void updateCountInfo(String videoId, String field, Integer changeCount);

    CountInfoDto selectSumCountInfo(String userId);

    void setRecommend(String videoId);
}
