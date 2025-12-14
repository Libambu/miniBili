package com.miniBili.controller;

import com.miniBili.api.consumer.ResourceClient;
import com.miniBili.entity.config.AppConfig;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.dto.VideoPlayDto;
import com.miniBili.entity.enums.DateTimePatternEnum;
import com.miniBili.entity.po.VideoInfoFile;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.exception.BusinessException;
import com.miniBili.utils.DateUtil;
import com.miniBili.utils.FFmpegUtils;
import com.miniBili.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

@RestController
@RequestMapping("/file/")
@Validated
@Slf4j
public class FileController extends ABaseController{

    @Autowired
    private ResourceClient resourceClient;


    @RequestMapping("uploadImage")
    public ResponseVO uploadImage(@NotNull MultipartFile file, @NotNull Boolean createThumbnail){
        return getSuccessResponseVO(resourceClient.uploadImage(file,createThumbnail));
    }

    @RequestMapping("/getResource")
    public void getResource(HttpServletResponse response,@NotNull String  sourceName){
        resourceClient.getResource(response, sourceName);
    }


    /**
     * 获取m3u8文件
     * @param response
     * @param fileId
     */
    @RequestMapping("/videoResource/{fileId}")
    public void videoResource(HttpServletResponse response, @PathVariable @NotEmpty String fileId){
        resourceClient.videoResource(response,fileId);
    }


    /**
     * 获取ts
     * @param response
     * @param fileId
     * @param ts
     */
    @RequestMapping("/videoResource/{fileId}/{ts}")
    public void videoResourceTs(HttpServletResponse response, @PathVariable @NotEmpty String fileId,@PathVariable @NotEmpty String ts){
        resourceClient.videoResourceTs(response,fileId,ts);
    }
}
