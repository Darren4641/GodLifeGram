package com.godlife.godlifegram.common.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExceptionMsg(
        String message,
        String code,
        boolean success,
        List<FieldErrorDetail> errors
) {
    public ExceptionMsg(String message, String code, boolean success) {
        this(message, code, success, List.of());
    }
}
