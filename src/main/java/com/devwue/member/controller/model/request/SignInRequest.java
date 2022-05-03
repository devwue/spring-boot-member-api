package com.devwue.member.controller.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(description = "로그인 요청 모델")
@Getter
public class SignInRequest {
    @Schema(description = "email", example = "test@user.com")
    @NotBlank(message = "email을 입력해 주세요.")
    @Email(message = "email 형식에 맞게 입력해 주세요.")
    private String email;

    @Schema(description = "비밀번호", example = "password")
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Size(min=8, max = 18, message = "비밀번호는 8 ~ 18자 이내로 입력해 주세요.")
    private String password;
}
