package com.godlife.godlifegram.user.application.converter;

import com.godlife.godlifegram.user.application.dto.request.SigninRequestSvcDto;
import com.godlife.godlifegram.user.application.dto.request.SignupRequestSvcDto;
import com.godlife.godlifegram.user.application.dto.response.SigninResponseSvcDto;
import com.godlife.godlifegram.user.domain.user.User;
import com.godlife.godlifegram.user.ui.dto.request.SigninRequestDto;
import com.godlife.godlifegram.user.ui.dto.request.SignupRequestDto;
import com.godlife.godlifegram.user.ui.dto.response.SigninResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    /** signup **/
    public SignupRequestSvcDto toSignupRequestSvcDto(SignupRequestDto signupRequestDto) {
        return new SignupRequestSvcDto(signupRequestDto.getEmail(), signupRequestDto.getPassword(), signupRequestDto.getNickname());
    }

    /** signin **/
    public SigninRequestSvcDto toSigninRequestSvcDto(SigninRequestDto signinRequestDto) {
        return new SigninRequestSvcDto(signinRequestDto.getEmail(), signinRequestDto.getPassword());
    }

    public SigninResponseDto toSigninResponseDto(SigninResponseSvcDto signinResponseSvcDto) {
        return new SigninResponseDto(signinResponseSvcDto.getEmail(), signinResponseSvcDto.getNickname());
    }

    public SigninResponseSvcDto toSigninResponseSvcDto(User user) {
        return new SigninResponseSvcDto(user.getEmail(), user.getNickname());
    }

}
