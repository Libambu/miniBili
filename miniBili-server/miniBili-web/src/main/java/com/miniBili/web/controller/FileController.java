package com.miniBili.web.controller;

import com.miniBili.component.RedisComponent;
import com.miniBili.entity.config.AppConfig;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.dto.SysSettingDto;
import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.dto.UploadingFileDto;
import com.miniBili.entity.enums.DateTimePatternEnum;
import com.miniBili.entity.enums.ResponseCodeEnum;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.exception.BusinessException;
import com.miniBili.utils.DateUtil;
import com.miniBili.utils.FFmpegUtils;
import com.miniBili.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("file/")
@Validated
@Slf4j
public class FileController extends ABaseController{
    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private FFmpegUtils fFmpegUtils;

    @Autowired
    private AppConfig appConfig;

    @RequestMapping("/getResource")
    public void getResource(HttpServletResponse response,@NotNull String  sourceName){
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

    /**
     *
     * @param fileName 文件名称
     * @param chunks 文件分片总数
     */
    @RequestMapping("/preUploadVideo")
    public ResponseVO preUploadVideo(@NotEmpty String fileName,@NotNull Integer chunks){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        String uploadId = redisComponent.savePreVideoFileInfo(tokenInfoDto.getUserId(),fileName,chunks);
        return getSuccessResponseVO(uploadId);
    }

    @RequestMapping("/uploadVideo")
    public ResponseVO uploadVideo(@NotNull MultipartFile chunkFile,@NotNull Integer chunkIndex,@NotEmpty String uploadId) throws IOException {
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        UploadingFileDto fileDto = redisComponent.getUploadVideoFile(tokenInfoDto.getUserId(),uploadId);
        if(fileDto==null){
            throw new BusinessException("文件不存在请重新上传");
        }
        SysSettingDto sysSettingDto = redisComponent.getSystemSetting();
        if(fileDto.getFileSize() > sysSettingDto.getVideoSize()*Constants.MB){
            throw new BusinessException("文件大小超过限制");
        }
        if((chunkIndex-1)>fileDto.getChunkIndex() || chunkIndex> fileDto.getChunks()-1){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        String folder= appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_TEMP + fileDto.getFilePath();
        chunkFile.transferTo(new File(folder + '/' + chunkIndex));
        fileDto.setFileSize(fileDto.getFileSize()+chunkFile.getSize());
        fileDto.setChunkIndex(chunkIndex);
        redisComponent.updateVideoFileInfo(tokenInfoDto.getUserId(),fileDto);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/delUploadVideo")
    public ResponseVO  delUploadVideo(String uploadId){
        TokenInfoDto tokenInfoDto = getTokenInfoDto();
        UploadingFileDto fileDto = redisComponent.getUploadVideoFile(tokenInfoDto.getUserId(),uploadId);
        if(fileDto==null){
            throw new BusinessException("文件不存在，请重新上传");
        }
        redisComponent.delVideoFileInfo(tokenInfoDto.getUserId(),uploadId);
        String filePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_TEMP + fileDto.getFilePath();
        File file = new File(filePath) ;
        deleteFile(file);
        return getSuccessResponseVO(uploadId);
    }


    /**
     *
     * @param file
     * @param createThumbnail 是否生成缩略图
     * @return
     */
    @RequestMapping("/uploadImage")
    public ResponseVO uploadImage(@NotNull MultipartFile file, @NotNull Boolean createThumbnail) throws IOException {
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
        String realName = StringTools.getRandomString(Constants.LENGTH_10) + fileSuffix;
        String filePath = folder + "/" + realName;
        file.transferTo(new File(filePath));
        if(createThumbnail){
            //如果要生成缩略图
            fFmpegUtils.createImageThumb(filePath);
        }
        return getSuccessResponseVO(Constants.FILE_COVER + month + "/" + realName);
    }

    private void deleteFile(File file){
        if(file.exists()){
            if(file.isFile()){
                file.delete();
            }else {
                File[] files = file.listFiles();
                for(File f : files){
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }

}
