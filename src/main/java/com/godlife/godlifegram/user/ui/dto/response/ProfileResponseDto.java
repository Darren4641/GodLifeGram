package com.godlife.godlifegram.user.ui.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {
    private Long id;
    private String email;
    private String nickname;
}
