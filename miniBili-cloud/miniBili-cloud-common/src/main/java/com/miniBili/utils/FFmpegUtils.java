package com.miniBili.utils;

import com.miniBili.entity.config.AppConfig;
import com.miniBili.entity.constants.Constants;
import com.miniBili.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;

@Component
public class FFmpegUtils {
    @Autowired
    private AppConfig appConfig;

    public void createImageThumb(String originPath){
        String CMD = "ffmpeg -i \"%s\" -vf scale=200:-1 \"%s\"";
        CMD = String.format(CMD, originPath,originPath+Constants.IMAGE_THUM_SUFFIX);
        ProcessUtils.executeCommand(CMD,true);
    }

    public Integer getVideoInfoTime(String completeVideo){
        String CMD_GET_TIME = "ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 \"%S\"";
        CMD_GET_TIME = String.format(CMD_GET_TIME,completeVideo);
        String result = ProcessUtils.executeCommand(CMD_GET_TIME, true);
        if(StringTools.isEmpty(result)){
            throw new BusinessException("获取视频时长出错");
        }
        result = result.replace("\n","");
        return new BigDecimal(result).intValue();
    }

    /**
     * 解析出视频编码
     * @param videoFilePath
     * @return
     */
    public  String getVideoCodec(String videoFilePath){
        String cmd = "ffprobe -v error -select_streams v:0 -show_entries stream=codec_name \"%S\"";
        String c = String.format(cmd,videoFilePath);
        String result = ProcessUtils.executeCommand(c,true);
        result = result.replace("\n","");
        result = result.substring(result.indexOf("=")+1);
        return result.substring(0,result.indexOf("["));
    }


    //这段代码“化整为零”：先把 mp4 转成一个完整的 .ts，再把它切成 30 秒一片的 .ts 并生成 .m3u8，最后把中间那个大 .ts 删掉，只留下 HLS 切片。
    //会将这个abc.mp4文件转成abc/u3m8 ts
    public void cutFile4Video(String tsFolder, String videoFilePath) {
        //把原始 MP4 文件无损地“换壳”成一个完整的 .ts 文件（临时文件）
        final String CMD_TRANSFER_2TS = "ffmpeg -y -i %s  -vcodec copy -acodec copy  %s";
        //将index.ts进行切片
        final String CMD_CUT_TS = "ffmpeg -i %s -c copy -map 0 -f segment -segment_list %s -segment_time 10 %s/%%4d.ts";
        String tsPath = tsFolder + "//" + Constants.TS_NAME;
        //生成.ts 把带占位符的字符串模板替换成真正的值，生成最终要用的字符串。
        String cmd = String.format(CMD_TRANSFER_2TS, videoFilePath, tsPath);
        ProcessUtils.executeCommand(cmd, false);
        //生成索引文件.m3u8 和切片.ts
        //它会按 30 秒一段 把 index.ts 切成若干小块，文件名固定用
        //0000.ts、0001.ts、0002.ts … 依次递增，4 位数字，补零。
        cmd = String.format(CMD_CUT_TS, tsPath, tsFolder + "/" + Constants.M3U8_NAME,tsFolder);
        ProcessUtils.executeCommand(cmd, false);
        //删除index.ts
        new File(tsPath).delete();
    }

    /**
     *  **HEVC（H.265） 编码的视频转成 H.264 编码的 MP4 文件**。
     * @param videoFilePath
     * @param newFileName
     */
    public void convertHevc2Mp4(String videoFilePath,String newFileName){
        String cmd_hevc_264 = "ffmpeg -i %s -c:v libx264 -crf 20 %s -y";
        String cmd = String.format(cmd_hevc_264,videoFilePath,newFileName);
        ProcessUtils.executeCommand(cmd,true);
    }


    public static void main(String[] args) {

        String videoFilePath = "E:\\program\\workspace\\miniBili\\file\\video\\2025-09-05\\小狗.mp4";
        String name = "mytest.mp4";
        String newFilename = videoFilePath+Constants.VIDEO_CODE_TEMP_SUFFIX;
        new File(videoFilePath).renameTo(new File(newFilename));
        //convertHevc2Mp4(newFilename,videoFilePath);
    }
}
