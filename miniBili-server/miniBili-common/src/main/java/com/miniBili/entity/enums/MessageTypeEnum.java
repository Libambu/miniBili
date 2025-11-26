package com.miniBili.entity.enums;

public enum MessageTypeEnum {

    SYS(1,"系统消息"),
    LIKE(2,"点赞"),
    COLLECTION(3,"收藏"),
    COMMENT(4,"评论");

    private Integer type;
    private String desc;
    MessageTypeEnum(Integer type,String desc){
        this.type  = type;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getType() {
        return type;
    }
    public static MessageTypeEnum getByType(Integer type){
        for(MessageTypeEnum messageTypeEnum : MessageTypeEnum.values()){
            if(messageTypeEnum.getType().equals(type)){
                return messageTypeEnum;
            }
        }
        return null;
    }
}
