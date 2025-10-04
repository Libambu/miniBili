package com.miniBili.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import com.miniBili.entity.enums.DateTimePatternEnum;
import com.miniBili.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;


/**
 * 
 */
public class UserVideoSeries implements Serializable {


	/**
	 * 列表id
	 */
	private Integer seriesId;

	/**
	 * 列表名称
	 */
	private String seriesName;

	/**
	 * 描述
	 */
	private String seriesDescription;

	/**
	 * 
	 */
	private String userId;

	/**
	 * 
	 */
	private Integer sort;

	/**
	 * 
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;


	public void setSeriesId(Integer seriesId){
		this.seriesId = seriesId;
	}

	public Integer getSeriesId(){
		return this.seriesId;
	}

	public void setSeriesName(String seriesName){
		this.seriesName = seriesName;
	}

	public String getSeriesName(){
		return this.seriesName;
	}

	public void setSeriesDescription(String seriesDescription){
		this.seriesDescription = seriesDescription;
	}

	public String getSeriesDescription(){
		return this.seriesDescription;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setSort(Integer sort){
		this.sort = sort;
	}

	public Integer getSort(){
		return this.sort;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public Date getUpdateTime(){
		return this.updateTime;
	}

	@Override
	public String toString (){
		return "列表id:"+(seriesId == null ? "空" : seriesId)+"，列表名称:"+(seriesName == null ? "空" : seriesName)+"，描述:"+(seriesDescription == null ? "空" : seriesDescription)+"，userId:"+(userId == null ? "空" : userId)+"，sort:"+(sort == null ? "空" : sort)+"，updateTime:"+(updateTime == null ? "空" : DateUtil.format(updateTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
	}
}
