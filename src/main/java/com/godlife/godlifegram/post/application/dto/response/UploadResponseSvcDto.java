package com.godlife.godlifegram.post.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponseSvcDto {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
}
