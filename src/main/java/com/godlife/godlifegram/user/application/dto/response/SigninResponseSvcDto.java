package com.godlife.godlifegram.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SigninResponseSvcDto {
    private Long id;
    private String email;
    private String nickname;
    private Boolean isPushEnabled;
}
