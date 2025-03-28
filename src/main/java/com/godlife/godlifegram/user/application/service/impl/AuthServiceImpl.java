package com.godlife.godlifegram.user.application.service.impl;


import com.godlife.godlifegram.common.exception.ApiErrorException;
import com.godlife.godlifegram.common.response.enums.ResultCode;
import com.godlife.godlifegram.user.application.converter.UserConverter;
import com.godlife.godlifegram.user.application.dto.request.SigninRequestSvcDto;
import com.godlife.godlifegram.user.application.dto.request.SignupRequestSvcDto;
import com.godlife.godlifegram.user.application.dto.response.SigninResponseSvcDto;
import com.godlife.godlifegram.user.application.service.AuthService;
import com.godlife.godlifegram.user.domain.user.User;
import com.godlife.godlifegram.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;



    @Override
    public SigninResponseSvcDto saveUserIfNot(SignupRequestSvcDto signupRequestSvcDto) {
        userRepository.findByEmail(signupRequestSvcDto.getEmail())
                .ifPresent(user -> { throw new ApiErrorException(ResultCode.ALREADY_SIGNUP); });

        User newUser = userRepository.save(User.signup(
                signupRequestSvcDto.getEmail(),
                signupRequestSvcDto.getNickname(),
                passwordEncoder.encode(signupRequestSvcDto.getPassword())));

        return userConverter.toSigninResponseSvcDto(newUser);
    }

    @Override
    public SigninResponseSvcDto getUser(SigninRequestSvcDto signinRequestSvcDto) {
        User user = userRepository.findByEmail(signinRequestSvcDto.getEmail())
                .orElseThrow(() -> new ApiErrorException(ResultCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(signinRequestSvcDto.getPassword(), user.getPassword())) {
            throw new ApiErrorException(ResultCode.INVALID_PASSWORD);
        }

        return userConverter.toSigninResponseSvcDto(user);
    }
}
