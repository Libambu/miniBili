package com.miniBili.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysSettingDto implements Serializable {
    private Integer registerCoinCount = 10;
    private Integer postVideoCoinCount = 5;
    private Integer videoSize = 10;
    /**
     * 文件批数，最多能发多少批
     */
    private Integer videoPCount = 10;
    private Integer videoCount = 10;
    private Integer commentCount = 20;
    private Integer danmuCount = 20;
}
