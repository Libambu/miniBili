package com.miniBili.api.provider;

import com.miniBili.api.consumer.VideoClient;
import com.miniBili.entity.config.AppConfig;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.dto.VideoPlayDto;
import com.miniBili.entity.enums.DateTimePatternEnum;
import com.miniBili.entity.po.VideoInfoFile;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.exception.BusinessException;
import com.miniBili.utils.DateUtil;
import com.miniBili.utils.FFmpegUtils;
import com.miniBili.utils.StringTools;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@Validated
@Slf4j
public class FileApi {

    @Autowired
    private AppConfig appConfig;
    @Autowired
    private FFmpegUtils fFmpegUtils;
    @Autowired
    private VideoClient videoClient;

    /**
     *
     * @param file
     * @param createThumbnail 是否生成缩略图
     * @return
     */
    @RequestMapping(value = Constants.Inner_api_prefix + "/file/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadImage(@NotNull MultipartFile file, @NotNull Boolean createThumbnail) throws IOException {
        String month = DateUtil.format(new Date(), DateTimePatternEnum.YYYY_MM_DD.getPattern());
        // COVER目录 E:/program/workspace/miniBili/file/cover/month
        String folder = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_COVER + month;
        File FolderFile = new File(folder);
        if(!FolderFile.exists()){
            FolderFile.mkdirs();
        }
        //主要是要获取文件后缀
        String fileName = file.getOriginalFilename();
        String fileSuffix = StringTools.getFileSuffix(fileName);
        String realName = StringTools.getRandomString(Constants.LENGTH_10) + "_" + fileName;
        String filePath = folder + "/" + realName;
        file.transferTo(new File(filePath));
        if(createThumbnail){
            //如果要生成缩略图
            fFmpegUtils.createImageThumb(filePath);
        }
        return Constants.FILE_COVER + month + "/" + realName;
    }

    @RequestMapping(Constants.Inner_api_prefix + "/file/getResource")
    public void getResource(HttpServletResponse response, @RequestParam String  sourceName){
        if(!StringTools.pathIsOK(sourceName)){
            throw  new BusinessException("文件路径有误");
        }
        String suffix = StringTools.getFileSuffix(sourceName);
        response.setContentType("image/" + suffix.replace(".",""));
        response.setHeader("Cache-Control","max-age=2592000");
        readFile(response,sourceName);
    }

    protected void readFile(HttpServletResponse response,String filePath){
        File file = new File(appConfig.getProjectFolder()+Constants.FILE_FOLDER + filePath);
        if(!file.exists()){
            return;
        }
        try(OutputStream out = response.getOutputStream(); FileInputStream in = new FileInputStream(file)){
            byte[] byteData = new byte[1024];
            int len = 0;
            while((len=in.read(byteData))!=-1){
                out.write(byteData,0,len);
            }
            out.flush();
        }catch (Exception e){
            log.error("文件读取异常");
        }
    }

    @RequestMapping(Constants.Inner_api_prefix + "/videoResource/{fileId}")
    void videoResource(HttpServletResponse response, @PathVariable String fileId){
        VideoInfoFile videoInfoFile = videoClient.getVideoInfoFileByFileId(fileId);
        VideoPlayDto videoPlayDto = new VideoPlayDto();
        videoPlayDto.setVideoId(videoInfoFile.getVideoId());
        videoPlayDto.setFileIndex(videoInfoFile.getFileIndex());
        String filePath = videoInfoFile.getFilePath();
        readFile(response,filePath + "/" + Constants.M3U8_NAME);
    }

    @RequestMapping(Constants.Inner_api_prefix + "/videoResource/{fileId}/{ts}")
    void videoResourceTs(HttpServletResponse response, @PathVariable String fileId, @PathVariable String ts){
        VideoInfoFile videoInfoFile = videoClient.getVideoInfoFileByFileId(fileId);
        String filePath = videoInfoFile.getFilePath();
        readFile(response,filePath + "/" + ts);
    }





}
