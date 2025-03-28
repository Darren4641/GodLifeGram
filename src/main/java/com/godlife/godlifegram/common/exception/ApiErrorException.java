package com.godlife.godlifegram.common.exception;

import com.godlife.godlifegram.common.response.enums.ResultCode;

public class ApiErrorException extends RuntimeException {
    private final ResultCode resultCode;

    public ApiErrorException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}