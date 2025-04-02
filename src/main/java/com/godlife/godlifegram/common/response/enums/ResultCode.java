package com.godlife.godlifegram.common.response.enums;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS("D-0", "OK"),
    ERROR("D-99", "관리자에게 문의해주세요."),
    INVALID_PARAMETER("D-01", "입력값이 올바르지 않습니다."),
    ALREADY_SIGNUP("D-02", "이미 회원가입된 계정입니다."),
    USER_NOT_FOUND("D-03", "가입된 계정이 없습니다."),
    INVALID_PASSWORD("D-04", "비밀번호가 일치하지 않습니다."),
    NOT_FOUND("D-05", "데이터를 찾을 수 없습니다."),
    ALREADY_DATA("D-06", "데이터가 이미 존재합니다."),
    FILE_SIZE_OVER_FIVE("D-07", "최대 5개까지 업로드 가능합니다."),

    SECURITY_ERROR("D-999", "인증에 실패하였습니다.");

    private final String code;
    private final String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
