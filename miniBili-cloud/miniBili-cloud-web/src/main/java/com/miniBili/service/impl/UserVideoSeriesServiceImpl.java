package com.miniBili.service.impl;

import com.miniBili.entity.enums.PageSize;
import com.miniBili.entity.enums.ResponseCodeEnum;
import com.miniBili.entity.po.UserVideoSeries;
import com.miniBili.entity.po.UserVideoSeriesVideo;
import com.miniBili.entity.po.VideoInfo;
import com.miniBili.entity.query.SimplePage;
import com.miniBili.entity.query.UserVideoSeriesQuery;
import com.miniBili.entity.query.UserVideoSeriesVideoQuery;
import com.miniBili.entity.query.VideoInfoQuery;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.exception.BusinessException;
import com.miniBili.mappers.UserVideoSeriesMapper;
import com.miniBili.mappers.UserVideoSeriesVideoMapper;
import com.miniBili.mappers.VideoInfoMapper;
import com.miniBili.service.UserVideoSeriesService;
import com.miniBili.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *  业务接口实现
 */
@Service("userVideoSeriesService")
public class UserVideoSeriesServiceImpl implements UserVideoSeriesService {

	@Resource
	private UserVideoSeriesMapper<UserVideoSeries, UserVideoSeriesQuery> userVideoSeriesMapper;
    @Autowired
    private VideoInfoMapper<VideoInfo,VideoInfoQuery> videoInfoMapper;
	@Autowired
	private UserVideoSeriesVideoMapper<UserVideoSeriesVideo, UserVideoSeriesVideoQuery> userVideoSeriesVideoMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserVideoSeries> findListByParam(UserVideoSeriesQuery param) {
		return this.userVideoSeriesMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(UserVideoSeriesQuery param) {
		return this.userVideoSeriesMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<UserVideoSeries> findListByPage(UserVideoSeriesQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<UserVideoSeries> list = this.findListByParam(param);
		PaginationResultVO<UserVideoSeries> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(UserVideoSeries bean) {
		return this.userVideoSeriesMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserVideoSeries> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userVideoSeriesMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserVideoSeries> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userVideoSeriesMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(UserVideoSeries bean, UserVideoSeriesQuery param) {
		StringTools.checkParam(param);
		return this.userVideoSeriesMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(UserVideoSeriesQuery param) {
		StringTools.checkParam(param);
		return this.userVideoSeriesMapper.deleteByParam(param);
	}

	/**
	 * 根据SeriesId获取对象
	 */
	@Override
	public UserVideoSeries getUserVideoSeriesBySeriesId(Integer seriesId) {
		return this.userVideoSeriesMapper.selectBySeriesId(seriesId);
	}

	/**
	 * 根据SeriesId修改
	 */
	@Override
	public Integer updateUserVideoSeriesBySeriesId(UserVideoSeries bean, Integer seriesId) {
		return this.userVideoSeriesMapper.updateBySeriesId(bean, seriesId);
	}

	/**
	 * 根据SeriesId删除
	 */
	@Override
	public Integer deleteUserVideoSeriesBySeriesId(Integer seriesId) {
		return this.userVideoSeriesMapper.deleteBySeriesId(seriesId);
	}

	@Override
	public List<UserVideoSeries> getUserAllSeries(String userId) {
		return userVideoSeriesMapper.selectUserAllSeries(userId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveUserSeries(UserVideoSeries videoSeries, String videoIds) {
		if(videoSeries.getSeriesId()==null&&StringTools.isEmpty(videoIds)){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if(videoSeries.getSeriesId()==null){
			//新增操作
			//先校验一下视频的id
			checkVideoIds(videoSeries.getUserId(),videoIds);
			videoSeries.setUpdateTime(new Date());
			videoSeries.setSort(userVideoSeriesMapper.selectMaxSort(videoSeries.getUserId())+1);
			userVideoSeriesMapper.insert(videoSeries);
			saveSeriesVideo(videoSeries.getUserId(),videoSeries.getSeriesId(),videoIds);
		}else {
			//修改的时候不允许修改视频内容
			UserVideoSeriesQuery userVideoSeriesQuery = new UserVideoSeriesQuery();
			userVideoSeriesQuery.setUserId(videoSeries.getUserId());
			userVideoSeriesQuery.setSeriesId(videoSeries.getSeriesId());
			userVideoSeriesMapper.updateByParam(videoSeries,userVideoSeriesQuery);
		}
	}

	private void  checkVideoIds(String userId,String videoIds){
		String[] videoIdArray = videoIds.split(",");
		VideoInfoQuery query = new VideoInfoQuery();
		query.setVideoIdArray(videoIdArray);
		query.setUserId(userId);
		Integer count = videoInfoMapper.selectCount(query);
		if(videoIdArray.length!=count){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
	}

	@Override
	public void saveSeriesVideo(String userId, Integer seriesId, String videoIds) {
		UserVideoSeries userVideoSeries = userVideoSeriesMapper.selectBySeriesId(seriesId);
		if(userVideoSeries==null||!userVideoSeries.getUserId().equals(userId)){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		checkVideoIds(userVideoSeries.getUserId(),videoIds);
		String[] videoIdArray = videoIds.split(",");
		Integer sort = userVideoSeriesVideoMapper.selectMaxSort(seriesId);
		List<UserVideoSeriesVideo> seriesVideos = new ArrayList<>();
		for(String videoId:videoIdArray){
			UserVideoSeriesVideo userVideoSeriesVideo = new UserVideoSeriesVideo();
			userVideoSeriesVideo.setSort(++sort);
			userVideoSeriesVideo.setSeriesId(seriesId);
			userVideoSeriesVideo.setUserId(userId);
			userVideoSeriesVideo.setVideoId(videoId);
			seriesVideos.add(userVideoSeriesVideo);
		}
		userVideoSeriesVideoMapper.insertOrUpdateBatch(seriesVideos);
	}

	@Override
	public void delSeriesVideo(String userId,Integer seriesId, String videoId) {
		UserVideoSeriesVideoQuery query = new UserVideoSeriesVideoQuery();
		query.setUserId(userId);
		query.setSeriesId(seriesId);
		query.setVideoId(videoId);
		userVideoSeriesVideoMapper.deleteByParam(query);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delVideoSeries(String userId, Integer seriesId) {
		UserVideoSeriesQuery query = new UserVideoSeriesQuery();
		query.setUserId(userId);
		query.setSeriesId(seriesId);
		Integer count = userVideoSeriesMapper.deleteByParam(query);
		if(count==0){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		UserVideoSeriesVideoQuery userVideoSeriesVideoQuery = new UserVideoSeriesVideoQuery();
		userVideoSeriesVideoQuery.setUserId(userId);
		userVideoSeriesVideoQuery.setSeriesId(seriesId);
		userVideoSeriesVideoMapper.deleteByParam(userVideoSeriesVideoQuery);
	}

	@Override
	public void changeVideoSeriesSort(String userId, String seriesIds) {
		String[] seriesArray = seriesIds.split(",");
		List<UserVideoSeries> videoSeriesList = new ArrayList<>();
		Integer sort = 0;
		for(String s : seriesArray){
			UserVideoSeries userVideoSeries = new UserVideoSeries();
			userVideoSeries.setUserId(userId);
			userVideoSeries.setSort(++sort);
			userVideoSeries.setSeriesId(Integer.parseInt(s));
			videoSeriesList.add(userVideoSeries);
		}
		userVideoSeriesMapper.changeSort(videoSeriesList);
	}

	@Override
	public List<UserVideoSeries> findListWithVideoList(UserVideoSeriesQuery seriesQuery) {
		return userVideoSeriesMapper.selectListWithVideo(seriesQuery);
	}
}