package com.miniBili.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.miniBili.component.RedisComponent;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.enums.StatisticTypeEnum;
import com.miniBili.entity.po.VideoInfo;
import com.miniBili.entity.query.UserInfoQuery;
import com.miniBili.entity.query.VideoInfoQuery;
import com.miniBili.mappers.UserFocusMapper;
import com.miniBili.mappers.UserInfoMapper;
import com.miniBili.mappers.VideoInfoMapper;
import com.miniBili.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniBili.entity.enums.PageSize;
import com.miniBili.entity.query.StatisticsInfoQuery;
import com.miniBili.entity.po.StatisticsInfo;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.query.SimplePage;
import com.miniBili.mappers.StatisticsInfoMapper;
import com.miniBili.service.StatisticsInfoService;
import com.miniBili.utils.StringTools;


/**
 *  业务接口实现
 */
@Service("statisticsInfoService")
public class StatisticsInfoServiceImpl implements StatisticsInfoService {

	@Autowired
	private RedisComponent redisComponent;

	@Resource
	private StatisticsInfoMapper<StatisticsInfo, StatisticsInfoQuery> statisticsInfoMapper;
    @Autowired
    private VideoInfoMapper<VideoInfo, VideoInfoQuery> videoInfoMapper;
    @Autowired
    private UserFocusMapper userFocusMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<StatisticsInfo> findListByParam(StatisticsInfoQuery param) {
		return this.statisticsInfoMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(StatisticsInfoQuery param) {
		return this.statisticsInfoMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<StatisticsInfo> findListByPage(StatisticsInfoQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<StatisticsInfo> list = this.findListByParam(param);
		PaginationResultVO<StatisticsInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(StatisticsInfo bean) {
		return this.statisticsInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<StatisticsInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.statisticsInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<StatisticsInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.statisticsInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(StatisticsInfo bean, StatisticsInfoQuery param) {
		StringTools.checkParam(param);
		return this.statisticsInfoMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(StatisticsInfoQuery param) {
		StringTools.checkParam(param);
		return this.statisticsInfoMapper.deleteByParam(param);
	}

	/**
	 * 根据StatisticsDateAndUserIdAndDataType获取对象
	 */
	@Override
	public StatisticsInfo getStatisticsInfoByStatisticsDateAndUserIdAndDataType(Date statisticsDate, String userId, Integer dataType) {
		return this.statisticsInfoMapper.selectByStatisticsDateAndUserIdAndDataType(statisticsDate, userId, dataType);
	}

	/**
	 * 根据StatisticsDateAndUserIdAndDataType修改
	 */
	@Override
	public Integer updateStatisticsInfoByStatisticsDateAndUserIdAndDataType(StatisticsInfo bean, Date statisticsDate, String userId, Integer dataType) {
		return this.statisticsInfoMapper.updateByStatisticsDateAndUserIdAndDataType(bean, statisticsDate, userId, dataType);
	}

	/**
	 * 根据StatisticsDateAndUserIdAndDataType删除
	 */
	@Override
	public Integer deleteStatisticsInfoByStatisticsDateAndUserIdAndDataType(Date statisticsDate, String userId, Integer dataType) {
		return this.statisticsInfoMapper.deleteByStatisticsDateAndUserIdAndDataType(statisticsDate, userId, dataType);
	}

	@Override
	public void statisticsData() {

		//下面大段全是在处理播放量逻辑，因为播放量是存在redis中，所以比较复杂
		List<StatisticsInfo>statisticsInfoList = new ArrayList<>();
		String statisticsDate = DateUtil.getBeforeDay(-1);
		//MinoBili:video:play:countOneDay:2025-11-29:videoId -> num
		Map<String, Integer> videoPlayCount = redisComponent.getVideoPlayCount(statisticsDate);
		List<String> playVideoKeys = new ArrayList<>(videoPlayCount.keySet());
		//List<videoId>
		playVideoKeys = playVideoKeys.stream().map(key->key.substring(key.lastIndexOf(":")+1)).collect(Collectors.toList());
		VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
		videoInfoQuery.setVideoIdArray(playVideoKeys.toArray(new String[playVideoKeys.size()]));
		//List<VideoInfo>
		List<VideoInfo>videoInfoList = videoInfoMapper.selectList(videoInfoQuery);
		Map<String,Integer>finalMap = videoInfoList.stream().collect(Collectors.groupingBy(VideoInfo::getUserId,
				Collectors.summingInt(item->videoPlayCount.get(Constants.REDIS_KEY_VIDEO_PLAY_COUNT+statisticsDate+":"+item.getVideoId()))));
		// 1. 先转成 LocalDate
		LocalDate localDate = LocalDate.parse(statisticsDate); // 默认格式就是 yyyy-MM-dd

		// 2. 再转成 java.util.Date
		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		finalMap.forEach((k,v)->{
			StatisticsInfo statisticsInfo = new StatisticsInfo();
			statisticsInfo.setUserId(k);
			statisticsInfo.setStatisticsCount(v);
			statisticsInfo.setDataType(StatisticTypeEnum.PLAY.getType());
			statisticsInfo.setStatisticsDate(date);
			statisticsInfoList.add(statisticsInfo);
		});


		//接下来统计粉丝数量
		List<StatisticsInfo> statisticsInfoListFans = statisticsInfoMapper.selectFans(statisticsDate);
		for(StatisticsInfo s : statisticsInfoListFans){
			s.setStatisticsDate(date);
			s.setDataType(StatisticTypeEnum.FANS.getType());
		}
		statisticsInfoList.addAll(statisticsInfoListFans);

		//接下来统计评论数量
		List<StatisticsInfo> statisticsInfoListComment = statisticsInfoMapper.selectComments(statisticsDate);
		for(StatisticsInfo s : statisticsInfoListComment){
			s.setStatisticsDate(date);
			s.setDataType(StatisticTypeEnum.COMMENT.getType());
		}
		statisticsInfoList.addAll(statisticsInfoListComment);

		//接下来统计 点赞，收藏，投币
		List<StatisticsInfo> statisticsInfoListOthers = statisticsInfoMapper.selectOthers(statisticsDate);
		for(StatisticsInfo s : statisticsInfoListOthers){
			s.setStatisticsDate(date);
		}
		statisticsInfoList.addAll(statisticsInfoListOthers);

		//最后统计弹幕数量
		List<StatisticsInfo> statisticsInfoListDanmu = statisticsInfoMapper.selectDanmu(statisticsDate);
		for(StatisticsInfo s : statisticsInfoListDanmu){
			s.setStatisticsDate(date);
			s.setDataType(StatisticTypeEnum.DANMU.getType());
		}
		statisticsInfoList.addAll(statisticsInfoListDanmu);

		statisticsInfoMapper.insertBatch(statisticsInfoList);
	}

	@Override
	public Map<String, Integer> getStatisticsInfoALL(String userId) {
		Map<String,Integer>map = statisticsInfoMapper.selectTotalCountInfo(userId);
		if(!StringTools.isEmpty(userId)){
			//用户端查粉丝数
			map.put("userCount",userFocusMapper.selectFansCount(userId));
		}else{
			//管理端查用户数
			map.put("userCount",userInfoMapper.selectCount(new UserInfoQuery()));
		}
		return map;
	}
}