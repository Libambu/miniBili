package com.miniBili.entity.enums;

public enum VideoFileUpdateTypeEnum {

    NO_UPDATE(0,"无更新"),
    UPDATE(1,"有更新");

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    private Integer status;
    private String desc;

   VideoFileUpdateTypeEnum(Integer status,String desc){
        this.status = status;
        this.desc =  desc;
    }

}
