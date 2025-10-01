package com.miniBili.component;

import com.miniBili.entity.config.AppConfig;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.dto.SysSettingDto;
import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.dto.UploadingFileDto;
import com.miniBili.entity.enums.DateTimePatternEnum;
import com.miniBili.entity.po.CategoryInfo;
import com.miniBili.entity.po.VideoInfoFilePost;
import com.miniBili.redis.RedisUtils;
import com.miniBili.utils.DateUtil;
import com.miniBili.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Negative;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class RedisComponent {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private RedisUtils redisUtils;

    public String saveCheckCode(String code){
        UUID uuid = UUID.randomUUID();
        String key =  uuid.toString();
        redisUtils.setex(Constants.REDIS_KEY_CHECK_CODE + key,code,Constants.REDIS_KEY_EXPIRE_ONE_MIN*10);
        return key;
    }

    public String getCheckCode(String checkCodeKey){
        return (String) redisUtils.get(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey);
    }

    public void cleanCheckCode(String checkCodeKey){
        redisUtils.delete(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey);
    }

    public TokenInfoDto saveTokenInfo(TokenInfoDto tokenInfoDto){
        String  token = UUID.randomUUID().toString();
        tokenInfoDto.setExpireAt(System.currentTimeMillis()+Constants.REDIS_KEY_EXPIRE_ONE_DAY*7l);
        tokenInfoDto.setToken(token);
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_WEB+token,tokenInfoDto,Constants.REDIS_KEY_EXPIRE_ONE_DAY*7l);
        return tokenInfoDto;
    }

    public void cleanToken(String token){
        redisUtils.delete(Constants.REDIS_KEY_TOKEN_WEB+token);
    }

    public TokenInfoDto getTokenInfoDtoByToken(String token){
        return (TokenInfoDto) redisUtils.get(Constants.REDIS_KEY_TOKEN_WEB+token);
    }

    public String saveTokenInfo4admin(String account){
        String  token = UUID.randomUUID().toString();
       redisUtils.setex(Constants.REDIS_KEY_TOKEN_ADMIN+token,account,Constants.REDIS_KEY_EXPIRE_ONE_DAY);
       return token;
    }


    public void cleanToken4Admin(String token){
        redisUtils.delete(Constants.REDIS_KEY_TOKEN_ADMIN+token);
    }


    public String  getToken4Admin(String token) {
        return (String) redisUtils.get(Constants.REDIS_KEY_TOKEN_ADMIN + token);
    }

    public void saveCategoryList(List<CategoryInfo> list){
        redisUtils.set(Constants.REDIS_KEY_CATEGORY_LIST,list);
    }

    public List<CategoryInfo> getCategoryList(){
        return (List)redisUtils.get(Constants.REDIS_KEY_CATEGORY_LIST);
    }

    public String savePreVideoFileInfo(String userId,String fileName,Integer chunks){
        String uploadId = StringTools.getRandomString(Constants.LENGTH_10);
        UploadingFileDto uploadingFileDto =  new UploadingFileDto();
        uploadingFileDto.setChunks(chunks);
        uploadingFileDto.setFileName(fileName);
        uploadingFileDto.setChunkIndex(0);
        uploadingFileDto.setUploadId(uploadId);
        String day = DateUtil.format(new Date(), DateTimePatternEnum.YYYY_MM_DD.getPattern());
        String filepath = day + "/" + userId +"_"+uploadId;
        String fileFolder = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_TEMP + filepath;
        File folderFile = new File(fileFolder);
        if(!folderFile.exists()){
            folderFile.mkdirs();
        }
        uploadingFileDto.setFilePath(filepath);
        redisUtils.setex(Constants.REDIS_KEY_UPLOADING_FILE + userId +"_" + uploadId,uploadingFileDto,Constants.REDIS_KEY_EXPIRE_ONE_DAY);
        return uploadId;
    }


    public UploadingFileDto getUploadVideoFile(String userId,String uploadId){
        return (UploadingFileDto) redisUtils.get(Constants.REDIS_KEY_UPLOADING_FILE + userId +"_" + uploadId);
    }

    public SysSettingDto getSystemSetting(){
        SysSettingDto dto = (SysSettingDto) redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
        if(dto==null){
            dto = new SysSettingDto();
        }
        return dto;
    }

    public void updateVideoFileInfo(String userId,UploadingFileDto fileDto){
        redisUtils.setex(Constants.REDIS_KEY_UPLOADING_FILE + userId +"_" + fileDto.getUploadId(),fileDto,Constants.REDIS_KEY_EXPIRE_ONE_DAY);
    }

    public void delVideoFileInfo(String userId,String uploadId){
        redisUtils.delete(Constants.REDIS_KEY_UPLOADING_FILE + userId +"_" + uploadId);
    }

    public void addFile2DelList(String videoId, List<String> filePath) {
        redisUtils.lpushAll(Constants.REDIS_KEY_FILE_DEL+videoId,filePath,Constants.REDIS_KEY_EXPIRE_ONE_DAY*7L);
    }

    public List<String> getDelFileList(String videoId) {
        return redisUtils.getQueueList(Constants.REDIS_KEY_FILE_DEL+videoId);
    }

    public void cleanDelFileList(String videoId) {
        redisUtils.delete(Constants.REDIS_KEY_FILE_DEL+videoId);
    }

    public void addFile2TransFerQueue(String videoId,List<VideoInfoFilePost> addFileList) {
        redisUtils.lpushAll(Constants.REDIS_KEY_QUEUE_TRANSFER ,addFileList,0);
    }

    public  VideoInfoFilePost getFileFromTransFerQueue(){
        return (VideoInfoFilePost)redisUtils.rpop(Constants.REDIS_KEY_QUEUE_TRANSFER);
    }



}
