package com.miniBili.entity.vo;

import com.miniBili.entity.po.VideoInfoFilePost;
import com.miniBili.entity.po.VideoInfoPost;
import lombok.Data;

import java.util.List;

@Data
public class VideoPostEditInfoVo {
    private VideoInfoPost videoInfo;
    private List<VideoInfoFilePost> videoInfoFileList;
}
