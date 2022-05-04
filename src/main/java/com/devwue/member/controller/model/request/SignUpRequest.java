package com.devwue.member.controller.model.request;

import com.devwue.member.annotation.Enum;
import com.devwue.member.model.enums.MobileAgency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.*;

@Schema(description = "회원 가입 요청 모델")
@Getter
@Builder
public class SignUpRequest {
    @Schema(description = "로그인시 사용하는 email 주소", example = "test@user.com")
    @NotBlank(message = "{email.notblank}")
    @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", message = "{email.pattern}")
    private String email;

    @Schema(description = "이름", example = "허준")
    @NotBlank(message = "{name.notblank}")
    @Size(min=2, max = 8, message = "{name.size}")
    private String name;

    @Schema(description = "닉네임", example = "명의")
    @NotBlank(message = "{nick-name.notblank}")
    @Size(min=2, max = 8, message = "{nick-name.size}")
    private String nickName;

    @Schema(description = "핸드폰 통신사", example = "SKT", allowableValues = {"SKT", "KT", "LGT", "SKM", "KTM", "LGM"})
    @NotBlank(message = "{phone-agency.notblank}")
    @Enum(enumClass = MobileAgency.class, message = "{phone-agency.enum}")
    private String phoneAgency;

    @Schema(description = "핸드폰 번호", example = "010-1234-5678")
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "{phone-number.pattern}")
    private String phoneNumber;

    @Schema(description = "로그인 비밀번호", example = "Hello7W@&rd")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,18}$", message = "{password.pattern}")
    private String password;

    @Schema(description = "로그인 비밀번호", example = "Hello7W@&rd")
    @NotBlank(message = "{password.validate}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,18}$", message = "{password.pattern}")
    private String passwordValidate;

    @AssertTrue(message = "{password.validate}")
    private boolean isValid() {
        return password != null && password.equals(passwordValidate);
    }
}
