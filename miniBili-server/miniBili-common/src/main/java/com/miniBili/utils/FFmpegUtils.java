package com.miniBili.utils;

import com.miniBili.entity.config.AppConfig;
import com.miniBili.entity.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FFmpegUtils {
    @Autowired
    private AppConfig appConfig;

    public void createImageThumb(String originPath){
        String CMD = "ffmpeg -i \"%s\" -vf scale=200:-1 \"%s\"";
        CMD = String.format(CMD, originPath,originPath+Constants.IMAGE_THUM_SUFFIX);
        ProcessUtils.executeCommand(CMD,true);
    }
}
