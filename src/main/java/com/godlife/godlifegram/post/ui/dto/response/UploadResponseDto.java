package com.godlife.godlifegram.post.ui.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
}
