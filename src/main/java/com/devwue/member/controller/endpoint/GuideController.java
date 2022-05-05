package com.devwue.member.controller.endpoint;

import com.devwue.member.annotation.Enum;
import com.devwue.member.model.enums.FeatureType;
import com.devwue.member.model.response.ApiResponse;
import com.devwue.member.service.PhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Tag(name = "가이드", description = "미 구현 기능 가이드")
@RestController
@Validated
@RequestMapping(value = "/guide", produces = {MediaType.APPLICATION_JSON_VALUE})
public class GuideController {
    private final PhoneService phoneService;

    public GuideController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @Operation(summary = "전화번호 인증 번호 확인", description = "", tags = "가이드")
    @GetMapping("/phone-message")
    public ResponseEntity<ApiResponse<String>> phoneMessage(@Parameter(example = "SIGN_UP", description = "SIGN_UP, RESET_PASSWORD")
                                                            @RequestParam("feature")
                                                            @Enum(enumClass = FeatureType.class, message = "{feature-type.enum}") String feature,
                                                            @Parameter(example = "010-1234-5678", description = "인증번호 발송 받은 전화번호")
                                                            @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "{phone-number.pattern}")
                                                            @RequestParam("phoneNumber") String phoneNumber) {
        return ResponseEntity
                .ok(ApiResponse.success(phoneService.getAuthString(feature, phoneNumber)));
    }
}
