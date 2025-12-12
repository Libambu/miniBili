package com.miniBili.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.miniBili.entity.enums.DateTimePatternEnum;
import com.miniBili.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 */
public class StatisticsInfo implements Serializable {


	/**
	 * 统计日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date statisticsDate;

	/**
	 * 
	 */
	private String userId;

	/**
	 * 数据统计类型
	 */
	private Integer dataType;

	/**
	 * 统计数量
	 */
	private Integer statisticsCount;


	public void setStatisticsDate(Date statisticsDate){
		this.statisticsDate = statisticsDate;
	}

	public Date getStatisticsDate(){
		return this.statisticsDate;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setDataType(Integer dataType){
		this.dataType = dataType;
	}

	public Integer getDataType(){
		return this.dataType;
	}

	public void setStatisticsCount(Integer statisticsCount){
		this.statisticsCount = statisticsCount;
	}

	public Integer getStatisticsCount(){
		return this.statisticsCount;
	}

	@Override
	public String toString (){
		return "统计日期:"+(statisticsDate == null ? "空" : DateUtil.format(statisticsDate, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，userId:"+(userId == null ? "空" : userId)+"，数据统计类型:"+(dataType == null ? "空" : dataType)+"，统计数量:"+(statisticsCount == null ? "空" : statisticsCount);
	}
}
