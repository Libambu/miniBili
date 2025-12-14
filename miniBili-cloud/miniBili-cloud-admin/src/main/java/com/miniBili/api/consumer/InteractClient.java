package com.miniBili.api.consumer;


import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.vo.PaginationResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = Constants.InteractName)
@Component
public interface InteractClient {
    @RequestMapping(Constants.Inner_api_prefix + "/interact/admin/loadComment")
    PaginationResultVO loadComment(@RequestParam Integer pageNo,
                                          @RequestParam String videoNameFuzzy);

    @RequestMapping(Constants.Inner_api_prefix + "/interact/admin/loadDanmu")
    PaginationResultVO loadDanmu(@RequestParam Integer pageNo,
                                        @RequestParam String videoNameFuzzy);
}
