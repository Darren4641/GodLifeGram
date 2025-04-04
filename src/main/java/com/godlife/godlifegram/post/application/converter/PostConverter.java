package com.godlife.godlifegram.post.application.converter;

import com.godlife.godlifegram.post.application.dto.request.LikeRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.response.LikeResponseSvcDto;
import com.godlife.godlifegram.post.application.dto.response.UploadResponseSvcDto;
import com.godlife.godlifegram.post.domain.Post;
import com.godlife.godlifegram.post.domain.PostLike;
import com.godlife.godlifegram.post.ui.dto.request.LikePostRequestDto;
import com.godlife.godlifegram.post.ui.dto.response.LikePostResponseDto;
import com.godlife.godlifegram.post.ui.dto.response.UploadResponseDto;
import org.springframework.stereotype.Component;

@Component
public class PostConverter {

    /** upload **/
    public UploadResponseDto toUploadResponseDto(UploadResponseSvcDto uploadResponseSvcDto) {
        return new UploadResponseDto(uploadResponseSvcDto.getId(), uploadResponseSvcDto.getContent(), uploadResponseSvcDto.getCreatedDate());
    }

    public UploadResponseSvcDto toUploadSvcResponseDto(Post post) {
        return new UploadResponseSvcDto(post.getId(), post.getContent(), post.getCreatedDate());
    }

    /** like **/
    public LikeRequestSvcDto toLikeSvcRequestDto(LikePostRequestDto likePostRequestDto) {
        return new LikeRequestSvcDto(likePostRequestDto.getPostId(), likePostRequestDto.getUuid(), likePostRequestDto.getIsLiked());
    }

    public LikePostResponseDto toLikeSvcResponseDto(LikeResponseSvcDto likeResponseSvcDto) {
        return new LikePostResponseDto(likeResponseSvcDto.getPostId(), likeResponseSvcDto.getUuid(), likeResponseSvcDto.getIsLiked());
    }

    public LikeResponseSvcDto toLikeResponseSvcDto(Long postId,
                                                   String uuid,
                                                   Boolean isLiked) {
        return new LikeResponseSvcDto(postId, uuid, isLiked);
    }
}
