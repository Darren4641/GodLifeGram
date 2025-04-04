package com.godlife.godlifegram.post.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponseSvcDto {
    private Long postId;
    private String uuid;
    private Boolean isLiked;
}
