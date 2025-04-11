package com.godlife.godlifegram.post.ui;

import com.godlife.godlifegram.common.exception.ApiErrorException;
import com.godlife.godlifegram.common.exception.UUIDErrorException;
import com.godlife.godlifegram.common.response.BaseResponse;
import com.godlife.godlifegram.common.response.enums.ResultCode;
import com.godlife.godlifegram.post.application.converter.PostConverter;
import com.godlife.godlifegram.post.application.dto.request.LikeRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.request.UploadRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.request.WriteCommentRequestSvcDto;
import com.godlife.godlifegram.post.application.dto.response.LikeResponseSvcDto;
import com.godlife.godlifegram.post.application.dto.response.UploadResponseSvcDto;
import com.godlife.godlifegram.post.application.dto.response.WriteCommentResponseSvcDto;
import com.godlife.godlifegram.post.application.service.PostService;
import com.godlife.godlifegram.post.infrastructure.S3Service;
import com.godlife.godlifegram.post.infrastructure.dto.ImageDto;
import com.godlife.godlifegram.post.ui.dto.request.LikePostRequestDto;
import com.godlife.godlifegram.post.ui.dto.request.UuidRequestDto;
import com.godlife.godlifegram.post.ui.dto.request.ViewCommentRequestDto;
import com.godlife.godlifegram.post.ui.dto.request.ViewPostRequestDto;
import com.godlife.godlifegram.post.ui.dto.response.*;
import com.godlife.godlifegram.user.application.dto.response.SigninResponseSvcDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final S3Service s3Service;
    private final PostService postService;
    private final PostConverter postConverter;

    @PostMapping
    public BaseResponse<UploadResponseDto> uploadPost(@RequestParam("content") String content,
                                                      @RequestParam("likeGoal") Long likeGoal,
                                                      @RequestParam("images") List<MultipartFile> images,
                                                      @SessionAttribute("user") SigninResponseSvcDto user) {
        if(images.size() > 5) {
            throw new ApiErrorException(ResultCode.FILE_SIZE_OVER_FIVE);
        }

        UploadRequestSvcDto serviceReqDto = new UploadRequestSvcDto(content, likeGoal, images, user);

        UploadResponseSvcDto serviceResDto = postService.upload(serviceReqDto);

        return new BaseResponse<>(postConverter.toUploadResponseDto(serviceResDto));
    }

    @GetMapping
    public BaseResponse<Page<ViewResponseDto>> viewPosts(@Valid @ModelAttribute ViewPostRequestDto viewPostRequestDto) {
        return new BaseResponse<>(postService.getPosts(viewPostRequestDto));
    }

    @PostMapping("/detail/{id}")
    public BaseResponse<ViewResponseDto> viewPost(@PathVariable(name = "id") Long id,
                                                  @RequestBody UuidRequestDto uuidRequestDto) {
        return new BaseResponse<>(postService.getPost(id, uuidRequestDto.getUuid()));
    }

    @PostMapping("/like")
    public BaseResponse<LikePostResponseDto> toggleLike(@RequestBody LikePostRequestDto likePostRequestDto) {
        if(likePostRequestDto.getUuid() == null || likePostRequestDto.getUuid().isEmpty()) {
            throw new UUIDErrorException();
        }

        LikeRequestSvcDto serviceReqDto = postConverter.toLikeSvcRequestDto(likePostRequestDto);

        LikeResponseSvcDto serviceResDto = postService.likeOrCancel(serviceReqDto);

        return new BaseResponse<>(postConverter.toLikeSvcResponseDto(serviceResDto));
    }

    @PostMapping("/comment")
    public BaseResponse<WriteCommentResponseDto> writeComment(@RequestParam("postId") Long postId,
                                                              @RequestParam("content") String content,
                                                              @SessionAttribute("user") SigninResponseSvcDto user) {

        WriteCommentRequestSvcDto serviceReqDto = new WriteCommentRequestSvcDto(postId, content, user);

        WriteCommentResponseSvcDto serviceResDto = postService.saveComment(serviceReqDto);

        return new BaseResponse<>(postConverter.toWriteCommentResponseDto(serviceResDto));
    }

    @GetMapping("/comment")
    public BaseResponse<Page<ViewCommentResponseDto>> viewComments(@Valid @ModelAttribute ViewCommentRequestDto viewPostRequestDto) {
        return new BaseResponse<>(postService.getComments(viewPostRequestDto));
    }


    @GetMapping("/image/{date}/{id}/{file}")
    public ResponseEntity<byte[]> getImageProxy(@PathVariable("date") String date,
                                                @PathVariable("id") String id,
                                                @PathVariable("file") String file) {
        String s3Key = String.format("%s/%s/%s", date, id, file);

        ImageDto imageDto = s3Service.getImage(s3Key);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, imageDto.getContent())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(imageDto.getImage());
    }
}
