package com.devwue.member.controller.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.*;

@Schema(description = "가입자 전화번호 인증 요청 모델")
@Getter
public class PhoneAuthRequest {
    @Schema(description = "email", example = "test@user.com")
    @NotBlank(message = "email을 입력해 주세요.")
    @Email(message = "email 형식으로 입력해 주세요.")
    private String email;

    @Schema(description = "회원 전화번호", example = "010-1234-5678")
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "번호는 10~11자리 숫자만 가능 합니다.")
    private String phoneNumber;

    @Schema(description = "인증문자", example = "123456")
    @NotBlank(message = "검증 번호를 입력해 주세요.")
    @Pattern(regexp = "^(\\d{6})$", message = "문자로 전달 받은 6자리 숫자를 입력 하세요.")
    private String phoneToken;
}
