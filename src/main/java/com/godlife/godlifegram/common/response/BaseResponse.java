package com.godlife.godlifegram.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.godlife.godlifegram.common.response.enums.ResultCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BaseResponse<T>(
        String resultCode,
        String message,
        boolean success,
        T data
) {

    /**
     * 요청 성공
     * @param data
     */
    public BaseResponse(T data) {
        this(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), true, data);
    }

    /**
     * 요청 실패
     * @param resultCode
     * @param data
     */
    public BaseResponse(ResultCode resultCode, T data) {
        this(resultCode.getCode(), resultCode.getMessage(), resultCode == ResultCode.SUCCESS, data);
    }

}