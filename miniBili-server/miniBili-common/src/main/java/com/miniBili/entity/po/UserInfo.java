package com.miniBili.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import com.miniBili.entity.enums.DateTimePatternEnum;
import com.miniBili.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;


/**
 * 用户信息表
 */
public class UserInfo implements Serializable {


	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 用户昵称
	 */
	private String nickName;

	/**
	 * 用户邮箱
	 */
	private String email;

	/**
	 * 用户密码
	 */
	private String password;

	/**
	 * 性别 0女1男2未知
	 */
	private Integer sex;

	/**
	 * 
	 */
	private String birthday;

	/**
	 * 学校
	 */
	private String school;

	/**
	 * 个人简介
	 */
	private String personIntroduction;

	/**
	 * 注册时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date joinTime;

	/**
	 * 最新登陆时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;

	/**
	 * 状态 0禁用 1正常
	 */
	private Integer status;

	/**
	 * 空间公告
	 */
	private String noticeInfo;

	/**
	 * 总硬币数
	 */
	private Integer totalCoinCount;

	/**
	 * 当前硬币数
	 */
	private Integer currentCoinCount;

	/**
	 * 主题
	 */
	private Integer theme;

	private String LastLoginIp;

	public String getLastLoginIp() {
		return LastLoginIp;
	}

	public void setLastLoginIp(String LastLoginIp) {
		this.LastLoginIp = LastLoginIp;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	public String getNickName(){
		return this.nickName;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return this.email;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return this.password;
	}

	public void setSex(Integer sex){
		this.sex = sex;
	}

	public Integer getSex(){
		return this.sex;
	}

	public void setBirthday(String birthday){
		this.birthday = birthday;
	}

	public String getBirthday(){
		return this.birthday;
	}

	public void setSchool(String school){
		this.school = school;
	}

	public String getSchool(){
		return this.school;
	}

	public void setPersonIntroduction(String personIntroduction){
		this.personIntroduction = personIntroduction;
	}

	public String getPersonIntroduction(){
		return this.personIntroduction;
	}

	public void setJoinTime(Date joinTime){
		this.joinTime = joinTime;
	}

	public Date getJoinTime(){
		return this.joinTime;
	}

	public void setLastLoginTime(Date lastLoginTime){
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastLoginTime(){
		return this.lastLoginTime;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setNoticeInfo(String noticeInfo){
		this.noticeInfo = noticeInfo;
	}

	public String getNoticeInfo(){
		return this.noticeInfo;
	}

	public void setTotalCoinCount(Integer totalCoinCount){
		this.totalCoinCount = totalCoinCount;
	}

	public Integer getTotalCoinCount(){
		return this.totalCoinCount;
	}

	public void setCurrentCoinCount(Integer currentCoinCount){
		this.currentCoinCount = currentCoinCount;
	}

	public Integer getCurrentCoinCount(){
		return this.currentCoinCount;
	}

	public void setTheme(Integer theme){
		this.theme = theme;
	}

	public Integer getTheme(){
		return this.theme;
	}

	@Override
	public String toString (){
		return "用户id:"+(userId == null ? "空" : userId)+"，用户昵称:"+(nickName == null ? "空" : nickName)+"，用户邮箱:"+(email == null ? "空" : email)+"，用户密码:"+(password == null ? "空" : password)+"，性别 0女1男2未知:"+(sex == null ? "空" : sex)+"，birthday:"+(birthday == null ? "空" : birthday)+"，学校:"+(school == null ? "空" : school)+"，个人简介:"+(personIntroduction == null ? "空" : personIntroduction)+"，注册时间:"+(joinTime == null ? "空" : DateUtil.format(joinTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，最新登陆时间:"+(lastLoginTime == null ? "空" : lastLoginTime)+"，状态 0禁用 1正常:"+(status == null ? "空" : status)+"，空间公告:"+(noticeInfo == null ? "空" : noticeInfo)+"，总硬币数:"+(totalCoinCount == null ? "空" : totalCoinCount)+"，当前硬币数:"+(currentCoinCount == null ? "空" : currentCoinCount)+"，主题:"+(theme == null ? "空" : theme);
	}
}
