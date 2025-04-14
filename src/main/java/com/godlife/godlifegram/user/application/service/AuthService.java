package com.godlife.godlifegram.user.application.service;


import com.godlife.godlifegram.user.application.dto.request.SigninRequestSvcDto;
import com.godlife.godlifegram.user.application.dto.request.SignupRequestSvcDto;
import com.godlife.godlifegram.user.application.dto.response.SigninResponseSvcDto;
import com.godlife.godlifegram.user.ui.dto.request.NotificationDto;
import com.godlife.godlifegram.user.ui.dto.response.ProfileResponseDto;

public interface AuthService {
    SigninResponseSvcDto saveUserIfNot(SignupRequestSvcDto signupRequestSvcDto);

    SigninResponseSvcDto getUser(SigninRequestSvcDto signinRequestSvcDto);

    ProfileResponseDto getUserProfile(Long id);

    void subscribe(NotificationDto notificationDto);
}
