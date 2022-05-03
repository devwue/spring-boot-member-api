package com.devwue.member.controller.endpoint;

import com.devwue.member.controller.model.request.*;
import com.devwue.member.model.response.*;
import com.devwue.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Any", description = "로그인하지 않은 사용자")
@RequestMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
@Validated
public class UserController {
    private final MemberService memberService;

    public UserController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원 가입", description = "회원 가입시 사용", tags = "Any")
    @PostMapping(value = "/signup")
    public ResponseEntity<ApiResponse<MemberDto>> signUp(@Validated @RequestBody SignUpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(memberService.signUp(request)));
    }

    @Operation(summary = "로그인", description = "가입자 대상 로그인", tags = "Any")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MemberTokenDto>> login(@Validated @RequestBody SignInRequest request) {
        return ResponseEntity.ok(ApiResponse.success(memberService.login(request)));
    }

    @Operation(summary = "가입자 계정 검색", description = "가입자 계정 또는 비밀번호를 잃어버린 경우", tags = "Any")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<MemberSearchDto>> search(@Validated @RequestBody SearchRequest request) {
        MemberSearchDto memberSearchDto = memberService.search(request);
        if (memberSearchDto != null) {
            return ResponseEntity.ok(ApiResponse.success(memberSearchDto));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.success());
        }
    }

    @Operation(summary = "가입자 전화번호 인증번호 발송", description = "계정 확인 후 가입자 확인을 위한 전화번호 인증번호 발송", tags = "Any")
    @PostMapping("/phone/token")
    public ResponseEntity<ApiResponse<?>> sendToken(@Validated @RequestBody PhoneTokenRequest request) {
        Boolean result = memberService.sendPhoneToken(request);
        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success());
        } else {
            return ResponseEntity.ok(ApiResponse.fail());
        }
    }

    @Operation(summary = "가입자 비밀번호 변경", description = "계정 전화번호 확인 및 비밀번호 변경", tags = "Any")
    @PutMapping("/change/password")
    public ResponseEntity<ApiResponse<MemberDto>> changePassword(@Validated @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(ApiResponse.success(memberService.verifyPhoneTokenWithChangePassword(request)));
    }
}
