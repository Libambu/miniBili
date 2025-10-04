package com.miniBili.entity.query;



/**
 * 参数
 */
public class UserVideoSeriesVideoQuery extends BaseParam {


	/**
	 * 列表id
	 */
	private Integer seriesId;

	/**
	 * 
	 */
	private String videoId;

	private String videoIdFuzzy;

	/**
	 * 
	 */
	private String userId;

	private String userIdFuzzy;

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

	public void setVideoIdFuzzy(String videoIdFuzzy){
		this.videoIdFuzzy = videoIdFuzzy;
	}

	public String getVideoIdFuzzy(){
		return this.videoIdFuzzy;
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

	public void setSort(Integer sort){
		this.sort = sort;
	}

	public Integer getSort(){
		return this.sort;
	}

}
