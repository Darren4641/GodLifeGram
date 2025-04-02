package com.godlife.godlifegram.post.application.service;

import com.godlife.godlifegram.post.application.dto.request.UploadRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.response.UploadResponseSvcDto;

public interface PostService {

    UploadResponseSvcDto upload(UploadRequestSvcDto uploadRequestSvcDto);
}
