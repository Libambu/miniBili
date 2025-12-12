package com.miniBili.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TokenInfoDto implements Serializable {

    private String userId;
    private String nickName;
    private String avatar;
    private Long expireAt;
    private String token;

    private Integer fansCount;
    private Integer currentCoinCount;
    private Integer focusCount;
}
