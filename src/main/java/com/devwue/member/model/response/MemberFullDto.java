package com.devwue.member.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "마이 페이지 응답 모델")
@Getter
@Builder
public class MemberFullDto {
    @Schema(description = "email", example = "test@user.com")
    private String email;
    @Schema(description = "이름", example = "허준")
    private String name;
    @Schema(description = "닉네임", example = "명의")
    private String nickName;
    @Schema(description = "핸드폰 번호", example = "0191235678")
    private String phoneNumber;
    @Schema(description = "핸드폰 통신사", example = "LGT")
    private String phoneAgency;
    @Schema(description = "핸드폰 인증 여부", example = "true")
    private Boolean phoneValidate;
    @Schema(description = "마지막 로그인 일시", nullable = true)
    private LocalDateTime signInAt;
    @Schema(description = "회원 가입 일시", example = "2022-05-03T19:30:45")
    private LocalDateTime createdAt;
}
