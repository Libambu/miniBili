package com.miniBili.api.consumer;

import com.miniBili.entity.constants.Constants;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;

@FeignClient(name = Constants.ResourceName)
@Component
public interface ResourceClient {
    @RequestMapping(value = Constants.Inner_api_prefix + "/file/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadImage(@RequestPart MultipartFile file, @RequestParam Boolean createThumbnail);

    @RequestMapping(Constants.Inner_api_prefix + "/file/getResource")
    public void getResource(HttpServletResponse response, @RequestParam String  sourceName);

    @RequestMapping(Constants.Inner_api_prefix + "/videoResource/{fileId}")
    void videoResource(HttpServletResponse response, @PathVariable String fileId);

    @RequestMapping(Constants.Inner_api_prefix + "/videoResource/{fileId}/{ts}")
    void videoResourceTs(HttpServletResponse response, @PathVariable String fileId, @PathVariable String ts);
}
