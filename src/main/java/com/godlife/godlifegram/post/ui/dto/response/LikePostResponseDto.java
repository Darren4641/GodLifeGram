package com.godlife.godlifegram.post.ui.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikePostResponseDto {
    private Long postId;
    private String uuid;
    private Boolean isLiked;
}


