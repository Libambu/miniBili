package com.miniBili.entity.vo;


import lombok.Data;

@Data
public class UserVo {
    private String userId;
    private String nickName;
    private String avatar;
    private Integer sex;
    /**
     * 个人简介
     */
    private String personIntroduction;
    /**
     * 空间公告
     */
    private String noticeInfo;
    /**
     * 等级
     */
    private Integer grade;

    private String birthday;

    private String school;

    private Integer fansCount;

    private Integer focusCount;

    private Integer likeCount;

    private Integer playCount;

    private Boolean haveFocus;

    private Integer theme;
}
