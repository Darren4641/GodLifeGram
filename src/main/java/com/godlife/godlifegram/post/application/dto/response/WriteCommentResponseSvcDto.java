package com.godlife.godlifegram.post.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WriteCommentResponseSvcDto {
    private Long commentId;
    private String content;
    private Long userId;
    private String nickname;
    private LocalDateTime createdDate;
}
