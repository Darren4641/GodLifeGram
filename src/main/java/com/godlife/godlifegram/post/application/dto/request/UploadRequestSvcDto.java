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
public class UploadRequestSvcDto {
    private String content;
    private Long likeGoal;
    private List<MultipartFile> images;
    private SigninResponseSvcDto user;
}
