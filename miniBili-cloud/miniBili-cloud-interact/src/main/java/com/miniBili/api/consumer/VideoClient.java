package com.miniBili.api.consumer;

import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.enums.SearchOrderTypeEnum;
import com.miniBili.entity.po.UserInfo;
import com.miniBili.entity.po.VideoInfo;
import com.miniBili.entity.po.VideoInfoPost;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = Constants.WebName)
@Component
public interface VideoClient {

    @RequestMapping(Constants.Inner_api_prefix + "/user/updateCoinCountInfo")
    Integer updateCoinCountInfo(@RequestParam String userId,@RequestParam Integer count);

    @RequestMapping(Constants.Inner_api_prefix + "/user/getUserInfoByUserId")
    UserInfo getUserInfoByUseId(@RequestParam String userId);

    @RequestMapping(Constants.Inner_api_prefix + "/video/getVideoInfoByVideoId")
    VideoInfo getVideoInfoByVideoId(@RequestParam String videoId);

    @RequestMapping(Constants.Inner_api_prefix + "/video/updateCountInfo")
    void updateCountInfo(@RequestParam String videoId,@RequestParam String fileId,@RequestParam Integer changeCount);

    @RequestMapping(Constants.Inner_api_prefix + "/video/getVideoInfoPostByVideoId")
    VideoInfoPost getVideoInfoPostByVideoId(@RequestParam String videoId);

    @RequestMapping(Constants.Inner_api_prefix + "/video/updateDocCount")
    void updateDocCount(@RequestParam String videoId, @RequestParam String fieldName,@RequestParam Integer changeCount);


}
