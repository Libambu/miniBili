package com.miniBili.entity.enums;



public enum StatisticTypeEnum {

    PLAY(0,"播放量"),
    FANS(1,"粉丝"),
    LIKE(2,"点赞量"),
    COLLECTION(3,"收藏量"),
    COIN(4,"硬币量"),
    COMMENT(5,"评论量"),
    DANMU(6,"弹幕量");

    private Integer type;
    private String desc;

    StatisticTypeEnum(Integer type,String desc){
        this.type = type;
        this.desc = desc;
    }

   public static StatisticTypeEnum getByType(Integer type){
        for(StatisticTypeEnum s : StatisticTypeEnum.values()){
            if(s.getType().equals(type)){
                return s;
            }
        }
        return null;
   }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
