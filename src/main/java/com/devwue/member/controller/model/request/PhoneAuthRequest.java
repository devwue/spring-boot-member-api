package com.devwue.member.controller.model.request;

import com.devwue.member.annotation.Enum;
import com.devwue.member.model.enums.FeatureType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.*;

@Schema(description = "전화번호 인증확인 요청 모델")
@Getter
public class PhoneAuthRequest {
    @Schema(description = "기능", example = "SIGN_UP", allowableValues = {"SIGN_UP", "RESET_PASSWORD"})
    @Enum(enumClass = FeatureType.class, message = "인증받을 서비스를 선택하세요.")
    private String feature;

    @Schema(description = "회원 전화번호", example = "010-1234-5678")
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "번호는 10~11자리 숫자만 가능 합니다.")
    private String phoneNumber;

    @Schema(description = "인증문자", example = "123456")
    @NotBlank(message = "검증 번호를 입력해 주세요.")
    @Pattern(regexp = "^(\\d{6})$", message = "문자로 전달 받은 6자리 숫자를 입력 하세요.")
    private String phoneToken;
}
