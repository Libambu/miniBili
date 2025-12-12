package com.miniBili.entity.enums;



public enum VideoOrderType {

    create_time(0,"create_time","最新发布"),
    play_count(1,"play_count","最多播放"),
    collect_count(2,"collect_count","最多收藏");

    private Integer type;
    private String field;
    private String desc;

    VideoOrderType(Integer type,String field,String desc){
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
    public static VideoOrderType getByType(Integer type){
        for(VideoOrderType V:VideoOrderType.values()){
            if (V.getType().equals(type)){
                return V;
            }
        }
        return null;
    }
}
