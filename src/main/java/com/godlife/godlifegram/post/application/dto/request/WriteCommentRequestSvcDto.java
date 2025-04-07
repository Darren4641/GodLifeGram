package com.godlife.godlifegram.post.application.dto.request;

import com.godlife.godlifegram.user.application.dto.response.SigninResponseSvcDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WriteCommentRequestSvcDto {
    private Long postId;
    private String content;
    private SigninResponseSvcDto user;
}
