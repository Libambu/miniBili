package com.miniBili.controller;

import com.miniBili.api.consumer.InteractClient;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interact")
@Validated
@Slf4j
public class interactController extends ABaseController{

    @Autowired
    private InteractClient interactClient;

    @RequestMapping("/loadComment")
    public ResponseVO loadComment(Integer pageNo,String videoNameFuzzy){
        PaginationResultVO resultVO = interactClient.loadComment(pageNo, videoNameFuzzy);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/loadDanmu")
    public ResponseVO loadDanmu(Integer pageNo,String videoNameFuzzy){
        PaginationResultVO resultVO = interactClient.loadDanmu(pageNo, videoNameFuzzy);
        return getSuccessResponseVO(resultVO);
    }

}
