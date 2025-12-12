package com.miniBili.entity.vo;

import lombok.Data;

@Data
public class VideoStatusCountInfo {
    private Integer auditPassCount;
    private Integer auditFailCount;
    private Integer inProgress;
}
