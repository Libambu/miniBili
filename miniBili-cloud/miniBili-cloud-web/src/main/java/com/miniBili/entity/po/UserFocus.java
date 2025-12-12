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
public class UserFocus implements Serializable {


	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 用户id
	 */
	private String focusUserId;

	/**
	 * 
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date focusTime;

	private String otherNickName;

	private String otherUserId;

	private String otherPersonIntroduction;

	private String otherAvatar;

	private Integer focusType;

	public String getOtherNickName() {
		return otherNickName;
	}

	public void setOtherNickName(String otherNickName) {
		this.otherNickName = otherNickName;
	}

	public String getOtherUserId() {
		return otherUserId;
	}

	public void setOtherUserId(String otherUserId) {
		this.otherUserId = otherUserId;
	}

	public String getOtherAvatar() {
		return otherAvatar;
	}

	public void setOtherAvatar(String otherAvatar) {
		this.otherAvatar = otherAvatar;
	}

	public String getOtherPersonIntroduction() {
		return otherPersonIntroduction;
	}

	public void setOtherPersonIntroduction(String otherPersonIntroduction) {
		this.otherPersonIntroduction = otherPersonIntroduction;
	}

	public Integer getFocusType() {
		return focusType;
	}

	public void setFocusType(Integer focusType) {
		this.focusType = focusType;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setFocusUserId(String focusUserId){
		this.focusUserId = focusUserId;
	}

	public String getFocusUserId(){
		return this.focusUserId;
	}

	public void setFocusTime(Date focusTime){
		this.focusTime = focusTime;
	}

	public Date getFocusTime(){
		return this.focusTime;
	}

	@Override
	public String toString (){
		return "用户id:"+(userId == null ? "空" : userId)+"，用户id:"+(focusUserId == null ? "空" : focusUserId)+"，focusTime:"+(focusTime == null ? "空" : DateUtil.format(focusTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
	}
}
