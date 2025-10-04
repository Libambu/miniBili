package com.miniBili.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;


/**
 * 
 */
public class UserVideoSeriesVideo implements Serializable {


	/**
	 * 列表id
	 */
	private Integer seriesId;

	/**
	 * 
	 */
	private String videoId;

	/**
	 * 
	 */
	private String userId;

	/**
	 * 
	 */
	private Integer sort;


	public void setSeriesId(Integer seriesId){
		this.seriesId = seriesId;
	}

	public Integer getSeriesId(){
		return this.seriesId;
	}

	public void setVideoId(String videoId){
		this.videoId = videoId;
	}

	public String getVideoId(){
		return this.videoId;
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

	@Override
	public String toString (){
		return "列表id:"+(seriesId == null ? "空" : seriesId)+"，videoId:"+(videoId == null ? "空" : videoId)+"，userId:"+(userId == null ? "空" : userId)+"，sort:"+(sort == null ? "空" : sort);
	}
}
