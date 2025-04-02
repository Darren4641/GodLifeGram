package com.godlife.godlifegram.post.application.service.impl;

import com.godlife.godlifegram.common.exception.ApiErrorException;
import com.godlife.godlifegram.common.response.enums.ResultCode;
import com.godlife.godlifegram.post.application.dto.request.UploadRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.response.UploadResponseSvcDto;
import com.godlife.godlifegram.post.application.service.PostService;
import com.godlife.godlifegram.post.domain.Post;
import com.godlife.godlifegram.post.domain.PostImageRepository;
import com.godlife.godlifegram.post.domain.PostRepository;
import com.godlife.godlifegram.post.infrastructure.S3Service;
import com.godlife.godlifegram.user.domain.user.User;
import com.godlife.godlifegram.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService  {
    private final S3Service service;

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    @Override
    public UploadResponseSvcDto upload(UploadRequestSvcDto uploadRequestSvcDto) {
        User user = userRepository.findByEmail(uploadRequestSvcDto.getUser().getEmail())
                .orElseThrow(() -> new ApiErrorException(ResultCode.USER_NOT_FOUND));

        Post newPost = Post.upload(uploadRequestSvcDto.getContent(), user);
        postRepository.save(newPost);


        return null;
    }
}
