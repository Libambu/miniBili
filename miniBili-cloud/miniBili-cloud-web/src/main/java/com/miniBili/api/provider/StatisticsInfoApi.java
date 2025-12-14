package com.miniBili.api.provider;


import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.dto.StatisticsInfoDto;
import com.miniBili.entity.enums.StatisticTypeEnum;
import com.miniBili.entity.po.StatisticsInfo;
import com.miniBili.entity.query.StatisticsInfoQuery;
import com.miniBili.entity.query.UserInfoQuery;
import com.miniBili.service.StatisticsInfoService;
import com.miniBili.service.impl.UserInfoServiceImpl;
import com.miniBili.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping(Constants.Inner_api_prefix)
@Slf4j
public class StatisticsInfoApi {
    @Autowired
    private StatisticsInfoService statisticsInfoService;
    @Autowired
    private UserInfoServiceImpl userInfoService;

    @RequestMapping("/statistics/admin/getActualTimeStatisticsInfo")
    public Map<String,Object> getActualTimeStatisticsInfo(){
        StatisticsInfoQuery statisticsInfoQuery = new StatisticsInfoQuery();
        String beforeDay = DateUtil.getBeforeDay(1);
        statisticsInfoQuery.setStatisticsDate(beforeDay);
        List<StatisticsInfo> list = statisticsInfoService.findTotalListByParam(statisticsInfoQuery);
        Integer userCount = userInfoService.findCountByParam(new UserInfoQuery());
        list.forEach(item->{
            if(StatisticTypeEnum.FANS.getType().equals(item.getDataType())){
                item.setStatisticsCount(userCount);
            }
        });
        //获取到各种类型昨天的数据
        Map<Integer,Integer> preDateMap = list.stream().collect(Collectors.toMap(k->k.getDataType(),
                v->v.getStatisticsCount(),(Oldv,NewV)->NewV));
        //还要把各种类型总的数据也查出来
        Map<String,Integer>totalStatistic = statisticsInfoService.getStatisticsInfoALL(null);
        Map<String,Object>result = new HashMap<>();
        result.put("preDayData",preDateMap);
        result.put("totalCountInfo",totalStatistic);
        return result;
    }

    @RequestMapping("/statistics/admin/getWeekStatisticsInfo")
    public List<StatisticsInfoDto> getWeekStatisticsInfo(@RequestParam Integer dataType){
        List<String>dataList = DateUtil.getBeforeDays(7);
        StatisticsInfoQuery statisticsInfoQuery = new StatisticsInfoQuery();
        statisticsInfoQuery.setDataType(dataType);
        statisticsInfoQuery.setStatisticsDateStart(dataList.get(0));
        statisticsInfoQuery.setStatisticsDateEnd(dataList.get(dataList.size()-1));
        statisticsInfoQuery.setOrderBy("statistics_date asc");
        List<StatisticsInfo>statisticsInfoList = null;
        if(!StatisticTypeEnum.FANS.getType().equals(dataType)){
            statisticsInfoList = statisticsInfoService.findTotalListByParam(statisticsInfoQuery);
        }else{
            statisticsInfoList = statisticsInfoService.findUserCountTotalInfoByParam(statisticsInfoQuery);
        }

        Map<String, StatisticsInfo> collect = statisticsInfoList.stream().collect(Collectors.toMap(k -> {
                    return Instant.ofEpochMilli(k.getStatisticsDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate().toString();
                },
                Function.identity()));
        List<StatisticsInfoDto>result = new ArrayList<>();
        for(String data : dataList){
            StatisticsInfo item = collect.get(data);
            StatisticsInfoDto statisticsInfoDto = new StatisticsInfoDto();
            statisticsInfoDto.setStatisticsDate(data);
            statisticsInfoDto.setDataType(dataType);
            if(item!=null){
                statisticsInfoDto.setStatisticsCount(item.getStatisticsCount());
            }else{
                statisticsInfoDto.setStatisticsCount(0);
            }
            result.add(statisticsInfoDto);
        }
        return result;
    }
}
