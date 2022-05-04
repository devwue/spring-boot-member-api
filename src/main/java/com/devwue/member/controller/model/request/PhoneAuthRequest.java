package com.devwue.member.controller.model.request;

import com.devwue.member.annotation.Enum;
import com.devwue.member.model.enums.FeatureType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.*;

@Schema(description = "전화번호 인증 확인 요청 모델")
@Getter
@AllArgsConstructor
public class PhoneAuthRequest {
    @Schema(description = "기능", example = "SIGN_UP", allowableValues = {"SIGN_UP", "RESET_PASSWORD"})
    @Enum(enumClass = FeatureType.class, message = "{feature-type.enum}")
    private String feature;

    @Schema(description = "회원 전화번호", example = "010-1234-5678")
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "{phone.pattern}")
    private String phoneNumber;

    @Schema(description = "인증문자", example = "123456")
    @NotBlank(message = "검증 번호를 입력해 주세요.")
    @Pattern(regexp = "^(\\d{6})$", message = "{phone.token}")
    private String phoneToken;
}
