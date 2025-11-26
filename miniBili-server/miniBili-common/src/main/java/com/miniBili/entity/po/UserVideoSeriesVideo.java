package com.miniBili.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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


	private String videoCover;

	private String videoName;

	private Integer playCount;


	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Data createTime;

	public String getVideoCover() {
		return videoCover;
	}

	public void setVideoCover(String videoCover) {
		this.videoCover = videoCover;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public Integer getPlayCount() {
		return playCount;
	}

	public void setPlayCount(Integer playCount) {
		this.playCount = playCount;
	}

	public Data getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Data createTime) {
		this.createTime = createTime;
	}

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
