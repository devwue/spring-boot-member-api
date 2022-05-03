package com.devwue.member.model.response;

import com.devwue.member.model.enums.ResultStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "공통 API 응답 모델")
@Getter
public class ApiResponse<T> {
    @Schema(description = "처리 결과 상태")
    private final ResultStatus status;
    @Schema(description = "처리 결과 메시지")
    private final String message;
    @Schema(description = "데이터 container")
    private final T data;

    private ApiResponse(ResultStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return of(ResultStatus.SUCCESS, ResultStatus.SUCCESS.getValue(), data);
    }

    public static <T> ApiResponse<T> success() {
        return of(ResultStatus.SUCCESS, ResultStatus.SUCCESS.getValue(), null);
    }

    public static <T> ApiResponse<T> fail(T data) {
        return of(ResultStatus.FAIL, ResultStatus.FAIL.getValue(), data);
    }

    public static <T> ApiResponse<T> fail(T data, String message) {
        return of(ResultStatus.FAIL, message, data);
    }

    public static<T> ApiResponse<T> fail(String message) {
        return of(ResultStatus.FAIL, message, null);
    }

    public static <T> ApiResponse<T> fail() {
        return of(ResultStatus.FAIL, ResultStatus.FAIL.getValue(), null);
    }

    public static <T> ApiResponse<T> of(ResultStatus resultStatus, String message, T data) {
        return new ApiResponse<>(resultStatus, message, data);
    }
}
