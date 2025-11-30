package com.miniBili.entity.dto;


import lombok.Data;

@Data
public class StatisticsInfoDto {
    private String statisticsDate;

    /**
     *
     */
    private String userId;

    /**
     * 数据统计类型
     */
    private Integer dataType;

    /**
     * 统计数量
     */
    private Integer statisticsCount;
}
