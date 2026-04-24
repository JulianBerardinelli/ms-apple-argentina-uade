package com.apple.tpo.e_commerce.dto.common;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private LocalDateTime timestamp;
    private Integer status;
    private Boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok(Integer status, String message, T data) {
        return new ApiResponse<>(LocalDateTime.now(), status, true, message, data);
    }

    public static <T> ApiResponse<T> error(Integer status, String message, T data) {
        return new ApiResponse<>(LocalDateTime.now(), status, false, message, data);
    }
}
