package com.godlife.godlifegram.post.application.service;

import com.godlife.godlifegram.post.application.dto.request.LikeRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.request.UploadRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.request.WriteCommentRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.response.LikeResponseSvcDto;
import com.godlife.godlifegram.post.application.dto.response.MyPostCountResponseSvcDto;
import com.godlife.godlifegram.post.application.dto.response.UploadResponseSvcDto;
import com.godlife.godlifegram.post.application.dto.response.WriteCommentResponseSvcDto;
import com.godlife.godlifegram.post.ui.dto.request.ViewCommentRequestDto;
import com.godlife.godlifegram.post.ui.dto.request.ViewPostRequestDto;
import com.godlife.godlifegram.post.ui.dto.response.MyPostResponseDto;
import com.godlife.godlifegram.post.ui.dto.response.ViewCommentResponseDto;
import com.godlife.godlifegram.post.ui.dto.response.ViewResponseDto;
import com.godlife.godlifegram.user.application.dto.response.SigninResponseSvcDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    UploadResponseSvcDto upload(UploadRequestSvcDto uploadRequestSvcDto);

    UploadResponseSvcDto reUpload(UploadRequestSvcDto uploadRequestSvcDto);

    void delete(Long id, SigninResponseSvcDto user);

    Page<ViewResponseDto> getPosts(ViewPostRequestDto viewPostRequestDto);

    ViewResponseDto getPost(Long id, String uuid);

    Page<ViewCommentResponseDto> getComments(ViewCommentRequestDto viewCommentRequestDto);

    LikeResponseSvcDto likeOrCancel(LikeRequestSvcDto likeRequestSvcDto, String serverBaseUrl);

    WriteCommentResponseSvcDto saveComment(WriteCommentRequestSvcDto writeCommentRequestSvcDto, String serverBaseUrl);

    MyPostCountResponseSvcDto getMyPostCount(Long id);

    List<MyPostResponseDto> getMyPost(Long id);
}
