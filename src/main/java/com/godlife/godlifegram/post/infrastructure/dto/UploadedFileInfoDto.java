package com.godlife.godlifegram.post.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadedFileInfoDto {
    private String url;
    private Double size;
}
