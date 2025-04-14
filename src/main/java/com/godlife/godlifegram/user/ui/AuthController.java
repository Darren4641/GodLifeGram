package com.godlife.godlifegram.user.ui;

import com.godlife.godlifegram.common.response.BaseResponse;
import com.godlife.godlifegram.user.application.converter.UserConverter;
import com.godlife.godlifegram.user.application.dto.request.SigninRequestSvcDto;
import com.godlife.godlifegram.user.application.dto.request.SignupRequestSvcDto;
import com.godlife.godlifegram.user.application.dto.response.SigninResponseSvcDto;
import com.godlife.godlifegram.user.application.service.AuthService;
import com.godlife.godlifegram.user.ui.dto.request.NotificationDto;
import com.godlife.godlifegram.user.ui.dto.request.SigninRequestDto;
import com.godlife.godlifegram.user.ui.dto.request.SignupRequestDto;
import com.godlife.godlifegram.user.ui.dto.response.SigninResponseDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserConverter userConverter;
    private final AuthService authService;

    @PostMapping("/login")
    public BaseResponse<SigninResponseDto> signin(@Valid @RequestBody SigninRequestDto signinRequestDto, HttpSession session) {
        SigninRequestSvcDto serviceReqDto = userConverter.toSigninRequestSvcDto(signinRequestDto);

        SigninResponseSvcDto serviceResDto = authService.getUser(serviceReqDto);

        session.setAttribute("user", serviceResDto);

        return new BaseResponse<>(userConverter.toSigninResponseDto(serviceResDto));
    }

    @PostMapping("/signup")
    public BaseResponse<SigninResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto, HttpSession session) {
        SignupRequestSvcDto serviceReqDto = userConverter.toSignupRequestSvcDto(signupRequestDto);

        SigninResponseSvcDto serviceResDto = authService.saveUserIfNot(serviceReqDto);

        session.setAttribute("user", serviceResDto);

        return new BaseResponse<>(userConverter.toSigninResponseDto(serviceResDto));
    }

    @PostMapping("/logout")
    public BaseResponse<String> logout(HttpSession session) {
        session.invalidate();
        return new BaseResponse<>("OK");
    }

    @PostMapping("/subscribe")
    public BaseResponse<?> subscribe(@RequestBody NotificationDto notificationDto,
                                     @SessionAttribute("user") SigninResponseSvcDto user) {
        notificationDto.setId(user.getId());
        authService.subscribe(notificationDto);
        return new BaseResponse<>("OK");
    }

    @PatchMapping("/subscribe")
    public BaseResponse<?> subscribe(@SessionAttribute("user") SigninResponseSvcDto user) {
        authService.updateSubscribe(user.getId());
        return new BaseResponse<>("OK");
    }


}
