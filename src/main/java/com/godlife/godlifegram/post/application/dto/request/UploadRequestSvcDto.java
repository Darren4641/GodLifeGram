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
    private Long id;
    private String content;
    private Long likeGoal;
    private List<MultipartFile> images;
    private SigninResponseSvcDto user;

    public UploadRequestSvcDto(String content, Long likeGoal, List<MultipartFile> images, SigninResponseSvcDto user) {
        this.content = content;
        this.likeGoal = likeGoal;
        this.images = images;
        this.user = user;
    }

    public void setPostId(Long id) {
        this.id = id;
    }
}
