package com.godlife.godlifegram.user.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestSvcDto {
    private String email;
    private String password;
    private String nickname;
}
