package com.godlife.godlifegram.post.application.converter;

import com.godlife.godlifegram.post.application.dto.response.UploadResponseSvcDto;
import com.godlife.godlifegram.post.domain.Post;
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
}
