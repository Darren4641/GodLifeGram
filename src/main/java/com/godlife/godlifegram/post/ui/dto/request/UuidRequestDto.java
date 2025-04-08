package com.godlife.godlifegram.post.ui.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UuidRequestDto {
    @NotBlank(message = "uuid는 필수 값입니다.")
    private String uuid;
}


