package com.godlife.godlifegram.post.ui.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViewCommentRequestDto {
    private Long postId;
    private int page = 0;
    private int size = 10;

    public Pageable getPageable() {
        return PageRequest.of(page, size);
    }
}


