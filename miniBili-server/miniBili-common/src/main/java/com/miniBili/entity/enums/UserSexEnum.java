package com.miniBili.entity.enums;

public enum UserSexEnum {
    women(0,"女"),
    man(1,"男"),
    secret(2,"保密");

    private Integer type;
    private String desc;

    UserSexEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static UserSexEnum getByType(Integer type){
        for(UserSexEnum item : UserSexEnum.values()){
            if(item.getType().equals(type)){
                return item;
            }
        }
        return null;
    }
}
