package com.miniBili.api.consumer;

import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.dto.StatisticsInfoDto;
import com.miniBili.entity.po.UserInfo;
import com.miniBili.entity.po.VideoInfoPost;
import com.miniBili.entity.query.UserInfoQuery;
import com.miniBili.entity.query.VideoInfoPostQuery;
import com.miniBili.entity.query.VideoInfoQuery;
import com.miniBili.entity.vo.PaginationResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = Constants.WebName)
@Component
public interface WebClient {

    @RequestMapping(Constants.Inner_api_prefix + "/statistics/admin/getActualTimeStatisticsInfo")
    Map<String,Object> getActualTimeStatisticsInfo();

    @RequestMapping(Constants.Inner_api_prefix + "/statistics/admin/getWeekStatisticsInfo")
    List<StatisticsInfoDto> getWeekStatisticsInfo(@RequestParam  Integer dataType);

    @RequestMapping(Constants.Inner_api_prefix + "/user/admin/getUserPage")
    PaginationResultVO<UserInfo> findListByPage(@RequestBody UserInfoQuery userInfoQuery);

    @RequestMapping(Constants.Inner_api_prefix + "/user/admin/getUserPage")
    PaginationResultVO findVideoInfoPostListByPage(@RequestBody VideoInfoPostQuery videoInfoPostQuery);

    @RequestMapping(Constants.Inner_api_prefix + "/video/loadVideoList")
    PaginationResultVO findListByPage(@RequestBody VideoInfoPostQuery videoInfoPostQuery);

    @RequestMapping(Constants.Inner_api_prefix + "/video/auditVideo")
    void aduitVideo(@RequestParam  String videoId,
                    @RequestParam  Integer status,
                    @RequestParam String reason);

    @RequestMapping(Constants.Inner_api_prefix + "/video/recommendVideo")
    void recommendVideo(@RequestParam String videoId);


    @RequestMapping(Constants.Inner_api_prefix + "/video/getVideoCount")
    Integer getVideoCount(@RequestBody VideoInfoQuery videoInfoQuery);
}
