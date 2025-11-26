package com.miniBili.entity.query;

import java.util.Date;


/**
 * 参数
 */
public class StatisticsInfoQuery extends BaseParam {


	/**
	 * 统计日期
	 */
	private String statisticsDate;

	private String statisticsDateStart;

	private String statisticsDateEnd;

	/**
	 * 
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 数据统计类型
	 */
	private Integer dataType;

	/**
	 * 统计数量
	 */
	private Integer statisticsCount;


	public void setStatisticsDate(String statisticsDate){
		this.statisticsDate = statisticsDate;
	}

	public String getStatisticsDate(){
		return this.statisticsDate;
	}

	public void setStatisticsDateStart(String statisticsDateStart){
		this.statisticsDateStart = statisticsDateStart;
	}

	public String getStatisticsDateStart(){
		return this.statisticsDateStart;
	}
	public void setStatisticsDateEnd(String statisticsDateEnd){
		this.statisticsDateEnd = statisticsDateEnd;
	}

	public String getStatisticsDateEnd(){
		return this.statisticsDateEnd;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setUserIdFuzzy(String userIdFuzzy){
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy(){
		return this.userIdFuzzy;
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

}
