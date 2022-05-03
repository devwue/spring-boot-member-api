package com.devwue.member.model.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "로그인 성공 응답 모델")
@Getter
@Builder
public class MemberTokenDto {
    @Schema(description = "email", example = "test@user.com")
    private String email;
    @Schema(description = "닉네임", example = "명의")
    private String nickName;
    @Schema(description = "핸드폰 인증 여부", example = "false")
    private Boolean phoneValidate;
    @Schema(description = "로그인 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
}
