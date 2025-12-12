package com.miniBili.api.consumer;

import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.po.VideoInfoFile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotEmpty;

@FeignClient(name = Constants.WebName)
@Component
public interface VideoClient {

    String apiPrefix = Constants.Inner_api_prefix+"/video";

    //Feign 默认会把没有注解的参数当成 “请求体”（@RequestBody）
    @RequestMapping(apiPrefix+"/getVideoInfoFileByFileId")
    VideoInfoFile getVideoInfoFileByFileId(@RequestParam("FileId")  @NotEmpty String FileId);

}
