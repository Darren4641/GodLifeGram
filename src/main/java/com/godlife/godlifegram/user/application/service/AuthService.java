package com.godlife.godlifegram.user.application.service;


import com.godlife.godlifegram.user.application.dto.request.SigninRequestSvcDto;
import com.godlife.godlifegram.user.application.dto.request.SignupRequestSvcDto;
import com.godlife.godlifegram.user.application.dto.response.SigninResponseSvcDto;

public interface AuthService {
    SigninResponseSvcDto saveUserIfNot(SignupRequestSvcDto signupRequestSvcDto);

    SigninResponseSvcDto getUser(SigninRequestSvcDto signinRequestSvcDto);
}
