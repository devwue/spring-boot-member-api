package com.devwue.member.controller.endpoint;

import com.devwue.member.controller.model.request.PhoneAuthRequest;
import com.devwue.member.model.response.ApiResponse;
import com.devwue.member.model.response.MemberFullDto;
import com.devwue.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원", description = "로그인 이후 이용 가능")
@RestController
@RequestMapping(value = "/member", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "마이 페이지", description = "로그인 사용자의 회원 정보", tags = "회원")
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<MemberFullDto>> memberPage(@RequestHeader(value="Authorization") String header) {
        MemberFullDto memberDto = memberService.info(header);
        if (memberDto != null) {
            return ResponseEntity.ok(ApiResponse.success(memberDto));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.success());
        }
    }
}
