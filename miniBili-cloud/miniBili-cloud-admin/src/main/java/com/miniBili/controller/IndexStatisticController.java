package com.miniBili.controller;

import com.miniBili.api.consumer.WebClient;
import com.miniBili.entity.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
@Validated
@Slf4j
public class IndexStatisticController extends ABaseController{

    @Autowired
    private WebClient webClient;

    @RequestMapping("/getActualTimeStatisticsInfo")
    public ResponseVO getActualTimeStatisticsInfo(){
        return getSuccessResponseVO(webClient.getActualTimeStatisticsInfo());
    }

    @RequestMapping("/getWeekStatisticsInfo")
    public ResponseVO getWeekStatisticsInfo(Integer dataType){
        return getSuccessResponseVO(webClient.getWeekStatisticsInfo(dataType));
    }
}
