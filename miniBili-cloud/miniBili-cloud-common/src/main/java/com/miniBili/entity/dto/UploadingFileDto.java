package com.miniBili.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadingFileDto implements Serializable {
    private String uploadId;
    private String fileName;
    private Integer chunkIndex;
    private Integer chunks;
    private Long fileSize = 0L;
    private String filePath;
}
