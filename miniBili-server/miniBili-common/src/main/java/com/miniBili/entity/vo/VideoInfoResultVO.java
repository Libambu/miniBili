package com.miniBili.entity.vo;

import com.miniBili.entity.po.VideoInfo;

import java.util.List;

public class VideoInfoResultVO {

    private VideoInfo videoInfo;

    private List userActionList;

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(VideoInfo videoInfo , List userActionList) {
        this.videoInfo = videoInfo;
        this.userActionList = userActionList;
    }

    public List getUserActionList() {
        return userActionList;
    }

    public void setUserActionList(List userActionList) {
        this.userActionList = userActionList;
    }
}
