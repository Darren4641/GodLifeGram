package com.godlife.godlifegram.post.application.service;

import com.godlife.godlifegram.post.application.dto.request.UploadRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.response.UploadResponseSvcDto;
import com.godlife.godlifegram.post.ui.dto.request.ViewPostRequestDto;
import com.godlife.godlifegram.post.ui.dto.response.ViewResponseDto;
import org.springframework.data.domain.Page;

public interface PostService {

    UploadResponseSvcDto upload(UploadRequestSvcDto uploadRequestSvcDto);

    Page<ViewResponseDto> getPosts(ViewPostRequestDto viewPostRequestDto);
}
