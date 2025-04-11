package com.godlife.godlifegram.post.application.converter;

import com.godlife.godlifegram.post.application.dto.request.LikeRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.response.LikeResponseSvcDto;
import com.godlife.godlifegram.post.application.dto.response.UploadResponseSvcDto;
import com.godlife.godlifegram.post.application.dto.response.WriteCommentResponseSvcDto;
import com.godlife.godlifegram.post.domain.Post;
import com.godlife.godlifegram.post.domain.PostComment;
import com.godlife.godlifegram.post.ui.dto.request.LikePostRequestDto;
import com.godlife.godlifegram.post.ui.dto.response.LikePostResponseDto;
import com.godlife.godlifegram.post.ui.dto.response.UploadResponseDto;
import com.godlife.godlifegram.post.ui.dto.response.WriteCommentResponseDto;
import com.godlife.godlifegram.user.domain.user.User;
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

    /** comment **/
    public WriteCommentResponseDto toWriteCommentResponseDto(WriteCommentResponseSvcDto writeCommentResponseSvcDto) {
        return new WriteCommentResponseDto(writeCommentResponseSvcDto.getCommentId(), writeCommentResponseSvcDto.getContent(), writeCommentResponseSvcDto.getUserId(), writeCommentResponseSvcDto.getNickname(), writeCommentResponseSvcDto.getCreatedDate());
    }

    public WriteCommentResponseSvcDto toWriteCommentSvcResponseDto(PostComment postComment, User user) {
        return new WriteCommentResponseSvcDto(postComment.getId(), postComment.getContent(), user.getId(), user.getNickname(), postComment.getCreatedDate());
    }
}
