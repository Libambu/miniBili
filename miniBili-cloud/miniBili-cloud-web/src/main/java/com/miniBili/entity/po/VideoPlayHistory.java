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
public class VideoPlayHistory implements Serializable {


	/**
	 * 
	 */
	private String userId;

	/**
	 * 视频id
	 */
	private String videoId;

	/**
	 * 文件索引
	 */
	private Integer fileIndex;

	private String videoName;

	private String videoCover;

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getVideoCover() {
		return videoCover;
	}

	public void setVideoCover(String videoCover) {
		this.videoCover = videoCover;
	}

	/**
	 * 最后更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastUpdateTime;


	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setVideoId(String videoId){
		this.videoId = videoId;
	}

	public String getVideoId(){
		return this.videoId;
	}

	public void setFileIndex(Integer fileIndex){
		this.fileIndex = fileIndex;
	}

	public Integer getFileIndex(){
		return this.fileIndex;
	}

	public void setLastUpdateTime(Date lastUpdateTime){
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getLastUpdateTime(){
		return this.lastUpdateTime;
	}

	@Override
	public String toString (){
		return "userId:"+(userId == null ? "空" : userId)+"，视频id:"+(videoId == null ? "空" : videoId)+"，文件索引:"+(fileIndex == null ? "空" : fileIndex)+"，最后更新时间:"+(lastUpdateTime == null ? "空" : DateUtil.format(lastUpdateTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
	}
}
