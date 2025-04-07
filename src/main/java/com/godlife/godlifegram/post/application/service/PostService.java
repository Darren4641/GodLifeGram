package com.godlife.godlifegram.post.application.service;

import com.godlife.godlifegram.post.application.dto.request.LikeRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.request.UploadRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.request.WriteCommentRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.response.LikeResponseSvcDto;
import com.godlife.godlifegram.post.application.dto.response.UploadResponseSvcDto;
import com.godlife.godlifegram.post.application.dto.response.WriteCommentResponseSvcDto;
import com.godlife.godlifegram.post.ui.dto.request.ViewPostRequestDto;
import com.godlife.godlifegram.post.ui.dto.response.ViewResponseDto;
import org.springframework.data.domain.Page;

public interface PostService {

    UploadResponseSvcDto upload(UploadRequestSvcDto uploadRequestSvcDto);

    Page<ViewResponseDto> getPosts(ViewPostRequestDto viewPostRequestDto);

    LikeResponseSvcDto likeOrCancel(LikeRequestSvcDto likeRequestSvcDto);

    WriteCommentResponseSvcDto saveComment(WriteCommentRequestSvcDto writeCommentRequestSvcDto);
}
