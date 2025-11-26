package com.miniBili.entity.dto;

import lombok.Data;

@Data
public class UserMessageExtendDto {
    private String messageContent;
    private String messageContentReply;
    private Integer auditStatus;
}
