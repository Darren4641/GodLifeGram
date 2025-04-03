package com.godlife.godlifegram.post.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageDto {
    private byte[] image;
    private String content;
}
