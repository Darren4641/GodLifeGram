package com.godlife.godlifegram.post.ui.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViewResponseDto {
    private Long id;
    private String content;
    private Long commentCount;
    private Long likeCount;
    private Long likeGoal;
    private String nickname;
    private List<String> images;
    private Boolean isLiked;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewDetailResponseDto {
        private Long id;
        private String content;
        private Long commentCount;
        private Long likeCount;
        private Long likeGoal;
        private String nickname;
        private String images;
        private Boolean isLiked;
        private LocalDateTime createdDate;

        public ViewResponseDto toViewResponseDto() {
            List<String> images = (this.images != null && this.images.length() != 0)
                    ? images = Arrays.stream(this.images.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList()
                    : Collections.emptyList();

            return new ViewResponseDto(
                    id,
                    content,
                    commentCount,
                    likeCount,
                    likeGoal,
                    nickname,
                    images,
                    isLiked,
                    createdDate
            );
        }
    }
}
