package com.miniBili.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class VideoPlayDto implements Serializable {
    private String videoId;
    private String userId;
    private Integer fileIndex;
}
