package com.godlife.godlifegram.post.application.service.impl;

import com.godlife.godlifegram.common.exception.ApiErrorException;
import com.godlife.godlifegram.common.response.enums.ResultCode;
import com.godlife.godlifegram.post.application.converter.PostConverter;
import com.godlife.godlifegram.post.application.dto.request.LikeRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.request.UploadRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.request.WriteCommentRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.response.LikeResponseSvcDto;
import com.godlife.godlifegram.post.application.dto.response.UploadResponseSvcDto;
import com.godlife.godlifegram.post.application.dto.response.WriteCommentResponseSvcDto;
import com.godlife.godlifegram.post.application.service.PostService;
import com.godlife.godlifegram.post.domain.*;
import com.godlife.godlifegram.post.infrastructure.S3Service;
import com.godlife.godlifegram.post.infrastructure.dto.UploadedFileInfoDto;
import com.godlife.godlifegram.post.ui.dto.request.ViewCommentRequestDto;
import com.godlife.godlifegram.post.ui.dto.request.ViewPostRequestDto;
import com.godlife.godlifegram.post.ui.dto.response.ViewCommentResponseDto;
import com.godlife.godlifegram.post.ui.dto.response.ViewResponseDto;
import com.godlife.godlifegram.user.domain.user.User;
import com.godlife.godlifegram.user.domain.user.UserRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final S3Service s3Service;
    private final PostConverter postConverter;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostRepositoryDsl postRepositoryDsl;
    private final PostImageRepository postImageRepository;

    @Override
    @Transactional
    public UploadResponseSvcDto upload(UploadRequestSvcDto uploadRequestSvcDto) {
        User user = userRepository.findByEmail(uploadRequestSvcDto.getUser().getEmail())
                .orElseThrow(() -> new ApiErrorException(ResultCode.USER_NOT_FOUND));

        Post newPost = Post.upload(uploadRequestSvcDto.getContent(), uploadRequestSvcDto.getLikeGoal(), user);
        postRepository.save(newPost);

        uploadRequestSvcDto.getImages().forEach(image -> {
            UploadedFileInfoDto uploaded = s3Service.upload(image, newPost.getId());
            PostImage postImage = PostImage.upload(newPost, uploaded.getUrl(), uploaded.getSize());
            postImageRepository.save(postImage);
        });

        return postConverter.toUploadSvcResponseDto(newPost);
    }

    @Override
    public Page<ViewResponseDto> getPosts(ViewPostRequestDto viewPostRequestDto) {
        return postRepositoryDsl.getPostsOfPage(viewPostRequestDto.getPageable(), viewPostRequestDto.getSortKeyword(), viewPostRequestDto.getUuid(), viewPostRequestDto.getSortDirection());
    }

    @Override
    public ViewResponseDto getPost(Long id, String uuid) {
        ViewResponseDto.ViewDetailResponseDto viewDetailResponseDto = postRepositoryDsl.getPost(id, uuid)
                .orElseThrow(() -> new ApiErrorException(ResultCode.NOT_FOUND));

        return viewDetailResponseDto.toViewResponseDto();
    }

    @Override
    public Page<ViewCommentResponseDto> getComments(ViewCommentRequestDto viewCommentRequestDto) {
        return postRepositoryDsl.getCommentsOfPage(viewCommentRequestDto.getPageable(), viewCommentRequestDto.getPostId());
    }

    @Override
    @Transactional
    public LikeResponseSvcDto likeOrCancel(LikeRequestSvcDto likeRequestSvcDto) {
        Post post = postRepository.findById(likeRequestSvcDto.getPostId())
                        .orElseThrow(() -> new ApiErrorException(ResultCode.NOT_FOUND));

        PostLike postLike = postLikeRepository.findByUuidAndPost(likeRequestSvcDto.getUuid(), post).orElse(null);


        if(postLike == null && likeRequestSvcDto.getIsLiked()) {
            PostLike newPostLike = PostLike.like(likeRequestSvcDto.getUuid(), post);
            postLikeRepository.save(newPostLike);
        } else if(postLike != null && !likeRequestSvcDto.getIsLiked()) {
            postLikeRepository.delete(postLike);
        } else {
            throw new ApiErrorException(ResultCode.ERROR);
        }

        return postConverter.toLikeResponseSvcDto(
                post.getId(),
                likeRequestSvcDto.getUuid(),
                likeRequestSvcDto.getIsLiked()
        );
    }

    @Override
    public WriteCommentResponseSvcDto saveComment(WriteCommentRequestSvcDto writeCommentRequestSvcDto) {
        User user = userRepository.findByEmail(writeCommentRequestSvcDto.getUser().getEmail())
                .orElseThrow(() -> new ApiErrorException(ResultCode.USER_NOT_FOUND));

        Post post = postRepository.findById(writeCommentRequestSvcDto.getPostId())
                .orElseThrow(() -> new ApiErrorException(ResultCode.NOT_FOUND));

        PostComment newComment = PostComment.writeComment(
                writeCommentRequestSvcDto.getContent(),
                user,
                post
                );

        postCommentRepository.save(newComment);

        return postConverter.toWriteCommentSvcResponseDto(newComment, user);
    }
}
