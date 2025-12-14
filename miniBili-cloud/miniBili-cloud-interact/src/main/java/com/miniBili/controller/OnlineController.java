package com.miniBili.controller;

import com.miniBili.component.RedisComponent;
import com.miniBili.entity.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/online")
@Validated
@Slf4j
public class OnlineController extends ABaseController{

    @Autowired
    private RedisComponent redisComponent;

    /**
     * 使用轮询获取在线人数
     * @param fileId
     * @param deviceId
     * @return
     */
    @RequestMapping("/reportVideoPlayOnline")
    public ResponseVO reportVideoPlayOnline(@NotEmpty String fileId, @NotEmpty String deviceId){
        return getSuccessResponseVO(redisComponent.reportVideoPlayOnline(fileId,deviceId));
    }
}
