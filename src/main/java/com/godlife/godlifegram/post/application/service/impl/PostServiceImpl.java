package com.godlife.godlifegram.post.application.service.impl;

import com.godlife.godlifegram.common.exception.ApiErrorException;
import com.godlife.godlifegram.common.response.enums.ResultCode;
import com.godlife.godlifegram.post.application.converter.PostConverter;
import com.godlife.godlifegram.post.application.dto.request.UploadRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.response.UploadResponseSvcDto;
import com.godlife.godlifegram.post.application.service.PostService;
import com.godlife.godlifegram.post.domain.*;
import com.godlife.godlifegram.post.infrastructure.S3Service;
import com.godlife.godlifegram.post.infrastructure.dto.UploadedFileInfoDto;
import com.godlife.godlifegram.post.ui.dto.request.ViewPostRequestDto;
import com.godlife.godlifegram.post.ui.dto.response.ViewResponseDto;
import com.godlife.godlifegram.user.domain.user.User;
import com.godlife.godlifegram.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService  {
    private final S3Service s3Service;
    private final PostConverter postConverter;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostRepositoryDsl postRepositoryDsl;
    private final PostImageRepository postImageRepository;

    @Override
    @Transactional
    public UploadResponseSvcDto upload(UploadRequestSvcDto uploadRequestSvcDto) {
        User user = userRepository.findByEmail(uploadRequestSvcDto.getUser().getEmail())
                .orElseThrow(() -> new ApiErrorException(ResultCode.USER_NOT_FOUND));

        Post newPost = Post.upload(uploadRequestSvcDto.getContent(), user);
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
        return postRepositoryDsl.getPostsOfPage(viewPostRequestDto.getPageable(), viewPostRequestDto.getSortKeyword(), viewPostRequestDto.getSortDirection());
    }
}
