package com.miniBili.entity.enums;

public enum UserStatusEnum {
    disable(0,"禁用"),
    enable(1,"启用");

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;

    UserStatusEnum(Integer status,String desc){
        this.desc = desc;
        this.status = status;
    }

    public static UserStatusEnum getByStatus(Integer status){
        for (UserStatusEnum item : UserStatusEnum.values()){
            if(item.getStatus() == status){
                return item;
            }
        }
        return null;
    }
}
