package com.godlife.godlifegram.post.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPostCountResponseSvcDto {
    private Long postCount;
    private Long likeCount;
}
