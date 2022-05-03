package com.devwue.member.controller.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Schema(description = "비밀번호 변경 요청 모델")
@Getter
public class ChangePasswordRequest {
    @Schema(description = "email", example = "test@user.com")
    @NotBlank(message = "email을 입력해 주세요.")
    @Email(message = "email 형식으로 입력해 주세요.")
    private String email;

    @Schema(description = "변경할 비밀번호", example = "password")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,18}$", message = "비밀번호는 영어와 숫자를 포함한 6~18자리 까지 가능 합니다.")
    private String password;

    @Schema(description = "변경할 비밀번호", example = "password")
    @NotBlank(message = "비밀번호를 동일하게 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,18}$", message = "비밀번호는 영어와 숫자를 포함한 6~18자리 까지 가능 합니다.")
    private String passwordValidate;

    @AssertTrue(message = "비밀번호를 동일하게 입력해 주세요.")
    private boolean isValid() {
        return password != null && password.equals(passwordValidate);
    }
}
