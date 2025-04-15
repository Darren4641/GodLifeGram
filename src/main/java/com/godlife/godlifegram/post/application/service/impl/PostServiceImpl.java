package com.godlife.godlifegram.post.application.service.impl;

import com.godlife.godlifegram.common.exception.ApiErrorException;
import com.godlife.godlifegram.common.response.enums.ResultCode;
import com.godlife.godlifegram.post.application.converter.PostConverter;
import com.godlife.godlifegram.post.application.dto.request.LikeRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.request.UploadRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.request.WriteCommentRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.response.LikeResponseSvcDto;
import com.godlife.godlifegram.post.application.dto.response.MyPostCountResponseSvcDto;
import com.godlife.godlifegram.post.application.dto.response.UploadResponseSvcDto;
import com.godlife.godlifegram.post.application.dto.response.WriteCommentResponseSvcDto;
import com.godlife.godlifegram.post.application.service.PostService;
import com.godlife.godlifegram.post.domain.*;
import com.godlife.godlifegram.post.infrastructure.S3Service;
import com.godlife.godlifegram.post.infrastructure.dto.UploadedFileInfoDto;
import com.godlife.godlifegram.post.ui.dto.request.ViewCommentRequestDto;
import com.godlife.godlifegram.post.ui.dto.request.ViewPostRequestDto;
import com.godlife.godlifegram.post.ui.dto.response.MyPostResponseDto;
import com.godlife.godlifegram.post.ui.dto.response.ViewCommentResponseDto;
import com.godlife.godlifegram.post.ui.dto.response.ViewResponseDto;
import com.godlife.godlifegram.user.application.dto.response.SigninResponseSvcDto;
import com.godlife.godlifegram.user.domain.user.User;
import com.godlife.godlifegram.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import nl.martijndwars.webpush.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final S3Service s3Service;
    private final PushNotificationService pushNotificationService;
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
    @Transactional
    public UploadResponseSvcDto reUpload(UploadRequestSvcDto uploadRequestSvcDto) {
        User user = userRepository.findByEmail(uploadRequestSvcDto.getUser().getEmail())
                .orElseThrow(() -> new ApiErrorException(ResultCode.USER_NOT_FOUND));

        Post post = postRepository.findById(uploadRequestSvcDto.getId())
                .orElseThrow(() -> new ApiErrorException(ResultCode.NOT_FOUND));

        List<PostImage> postImages = postImageRepository.findByPostId(post.getId());
        postImages.forEach(image -> {
            String s3Key = image.getImageUrl().replaceFirst("^/post/image/", "");
            s3Service.deleteFile(s3Key);
            postImageRepository.delete(image);
        });

        post.reUpload(uploadRequestSvcDto.getContent(), uploadRequestSvcDto.getLikeGoal(), user);

        uploadRequestSvcDto.getImages().forEach(image -> {
            UploadedFileInfoDto uploaded = s3Service.upload(image, post.getId());
            PostImage postImage = PostImage.upload(post, uploaded.getUrl(), uploaded.getSize());
            postImageRepository.save(postImage);
        });

        return postConverter.toUploadSvcResponseDto(post);
    }

    @Override
    @Transactional
    public void delete(Long id, SigninResponseSvcDto user) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ApiErrorException(ResultCode.NOT_FOUND));

        List<PostImage> postImages = postImageRepository.findByPostId(id);
        postImages.forEach(image -> {
            String s3Key = image.getImageUrl().replaceFirst("^/post/image/", "");
            s3Service.deleteFile(s3Key);
            postImageRepository.delete(image);
        });

        postCommentRepository.deleteByPost(post);
        postLikeRepository.deleteByPost(post);
        postRepository.delete(post);
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
    public LikeResponseSvcDto likeOrCancel(LikeRequestSvcDto likeRequestSvcDto, String serverBaseUrl) {
        Post post = postRepository.findById(likeRequestSvcDto.getPostId())
                        .orElseThrow(() -> new ApiErrorException(ResultCode.NOT_FOUND));

        User writer = userRepository.findById(post.getUser().getId())
                .orElseThrow(() -> new ApiErrorException(ResultCode.USER_NOT_FOUND));

        PostLike postLike = postLikeRepository.findByUuidAndPost(likeRequestSvcDto.getUuid(), post).orElse(null);


        if(postLike == null && likeRequestSvcDto.getIsLiked()) {
            PostLike newPostLike = PostLike.like(likeRequestSvcDto.getUuid(), post);
            postLikeRepository.save(newPostLike);

            if(writer.getIsPushEnabled()) {
                Subscription subscription = new Subscription(
                        writer.getEndPoint(),
                        new Subscription.Keys(writer.getP256dh(), writer.getAuth()));

                pushNotificationService.sendPushNotificationFromLike(subscription, serverBaseUrl, post.getId());
            }


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
    public WriteCommentResponseSvcDto saveComment(WriteCommentRequestSvcDto writeCommentRequestSvcDto, String serverBaseUrl) {
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

        User writer = userRepository.findById(post.getUser().getId())
                .orElseThrow(() -> new ApiErrorException(ResultCode.NOT_FOUND));

        if(writer.getIsPushEnabled()) {
            Subscription subscription = new Subscription(
                    writer.getEndPoint(),
                    new Subscription.Keys(writer.getP256dh(), writer.getAuth()));

            pushNotificationService.sendPushNotificationFromComment(subscription, serverBaseUrl, post.getId(), user.getNickname());
        }

        return postConverter.toWriteCommentSvcResponseDto(newComment, user);
    }

    @Override
    public MyPostCountResponseSvcDto getMyPostCount(Long id) {
        return postRepositoryDsl.myPostCount(id);
    }

    public List<MyPostResponseDto> getMyPost(Long id) {
        return postRepositoryDsl.getPostById(id);
    }
}
