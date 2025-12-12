package com.miniBili.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class VideoInfoEsDto {
    /**
     * 视频id
     */
    private String videoId;

    /**
     * 视频封面
     */
    private String videoCover;

    /**
     * 视频名称
     */
    private String videoName;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    /**
     * 标签
     */
    private String tags;



    /**
     * 播放量
     */
    private Integer playCount;



    /**
     * 弹幕数量
     */
    private Integer danmuCount;



    /**
     * 收藏数
     */
    private Integer collectCount;


}
