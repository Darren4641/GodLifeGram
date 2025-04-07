package com.godlife.godlifegram.post.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WriteCommentResponseSvcDto {
    private Long postId;
    private String content;
    private LocalDateTime createdDate;
}
