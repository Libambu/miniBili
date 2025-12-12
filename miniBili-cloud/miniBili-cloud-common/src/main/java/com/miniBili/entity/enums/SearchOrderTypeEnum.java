package com.miniBili.entity.enums;

public enum SearchOrderTypeEnum {

    VIDEO_PLAY(0,"playCount","视频播放量"),
    VIDEO_TIME(1,"createTime","视频创建时间"),
    VIDEO_DANMU(2,"danmuCount","弹幕数量"),
    VIDEO_COLLECT(3,"collectCount","收擦数量");

    private Integer type;
    private String field;
    private String desc;

    SearchOrderTypeEnum(Integer type,String field,String desc){
        this.type = type;
        this.field = field;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getField() {
        return field;
    }

    public String getDesc() {
        return desc;
    }

    public static SearchOrderTypeEnum getByType(Integer type){
        for(SearchOrderTypeEnum s : SearchOrderTypeEnum.values()){
            if(s.getType().equals(type)){
                return s;
            }
        }
        return null;
    }
}
