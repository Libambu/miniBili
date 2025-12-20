package com.miniBili.task;


import com.miniBili.service.StatisticsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SysTask {

    @Autowired
    private StatisticsInfoService statisticsInfoService;

    /**
     * 每天凌晨12点自动处理
     */

    //@Scheduled(cron = "0 * * * * *")
    @Scheduled(cron = "0 0 0 * * *")
    public void statisticsData(){
        statisticsInfoService.statisticsData();
    }
}
