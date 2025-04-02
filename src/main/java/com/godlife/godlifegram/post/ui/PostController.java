package com.godlife.godlifegram.post.ui;

import com.godlife.godlifegram.common.exception.ApiErrorException;
import com.godlife.godlifegram.common.response.BaseResponse;
import com.godlife.godlifegram.common.response.enums.ResultCode;
import com.godlife.godlifegram.post.application.converter.PostConverter;
import com.godlife.godlifegram.post.application.dto.request.UploadRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.response.UploadResponseSvcDto;
import com.godlife.godlifegram.post.application.service.PostService;
import com.godlife.godlifegram.post.ui.dto.response.UploadResponseDto;
import com.godlife.godlifegram.user.application.dto.response.SigninResponseSvcDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final PostConverter postConverter;

    @PostMapping
    public BaseResponse<UploadResponseDto> updatePost(@RequestParam("content") String content,
                                                      @RequestParam("images") List<MultipartFile> images,
                                                      @SessionAttribute("user") SigninResponseSvcDto user) {
        if(images.size() > 5) {
            throw new ApiErrorException(ResultCode.FILE_SIZE_OVER_FIVE);
        }

        UploadRequestSvcDto serviceReqDto = new UploadRequestSvcDto(content, images, user);

        UploadResponseSvcDto serviceResDto = postService.upload(serviceReqDto);

        return new BaseResponse<>(postConverter.toUploadResponseDto(serviceResDto));
    }
}
