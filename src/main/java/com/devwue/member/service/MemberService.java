package com.devwue.member.service;

import com.devwue.member.controller.model.request.*;
import com.devwue.member.exception.AuthException;
import com.devwue.member.exception.DuplicationException;
import com.devwue.member.exception.NotAcceptableException;
import com.devwue.member.exception.NotFoundException;
import com.devwue.member.model.entity.Member;
import com.devwue.member.model.entity.PhoneAuthentication;
import com.devwue.member.model.enums.FeatureType;
import com.devwue.member.model.enums.IdentifierType;
import com.devwue.member.model.enums.MessageAuthStatus;
import com.devwue.member.model.response.*;
import com.devwue.member.repository.MemberRepository;
import com.devwue.member.repository.PhoneAuthenticationRepository;
import com.devwue.member.support.JwtUtil;
import com.devwue.member.support.StringFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhoneAuthenticationRepository phoneAuthenticationRepository;
    private final PhoneService phoneService;

    public MemberService(MemberRepository memberRepository,
                         PasswordEncoder passwordEncoder,
                         PhoneAuthenticationRepository phoneAuthenticationRepository,
                         PhoneService phoneService) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.phoneAuthenticationRepository = phoneAuthenticationRepository;
        this.phoneService = phoneService;
    }

    @Transactional
    public MemberDto signUp(SignUpRequest request) {
        String phoneNumber = StringFilter.onlyNumber(request.getPhoneNumber());

        LocalDateTime before10Minute = LocalDateTime.now().minusMinutes(10);
        phoneAuthenticationRepository.findTopByFeatureAndPhoneNumberAndStatusAndCreatedAtAfterOrderByIdDesc(FeatureType.SIGN_UP.name(), phoneNumber, 2, before10Minute)
                .orElseThrow(() -> new NotFoundException("전화번호 인증을 먼저 받아야 합니다."));

        final Optional<Member> emailMember = memberRepository.findByEmail(request.getEmail());
        if (emailMember.isPresent()) {
            throw new DuplicationException("이미 email 사용자가 있습니다.");
        }

        final Optional<Member> nickMember = memberRepository.findByNickName(request.getNickName());
        if (nickMember.isPresent()) {
            throw new DuplicationException("동일한 닉네임을 사용하는 사용자가 있습니다.");
        }

        final Optional<Member> phoneMember = memberRepository.findByPhoneNumber(phoneNumber);
        if (phoneMember.isPresent()) {
            throw new DuplicationException("이미 인증된 phone 사용자가 있습니다.");
        }

        final Member member = new Member().toMember(request);
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        return memberRepository.save(member)
                .toMemberDto();
    }

    public MemberTokenDto login(SignInRequest request) {
        final Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("사용자가 없습니다."));

        boolean matched = passwordEncoder.matches(request.getPassword(), member.getPassword());
        if (!matched) {
            throw new AuthException("비밀번호가 일치하지 않습니다.");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getEmail(), null, null);
        String token = JwtUtil.newToken(authentication);
        updateLoginAt(member);

        return MemberTokenDto.builder()
                .token(token)
                .email(member.getEmail())
                .nickName(member.getNickName())
                .phoneValidate(member.getPhoneValidate().equals(true))
                .build();
    }

    public void verifyPhoneToken(PhoneAuthRequest request) {
        final FeatureType featureType = FeatureType.valueOf(request.getFeature());
        final String phoneNumber = StringFilter.onlyNumber(request.getPhoneNumber());

        PhoneAuthentication phoneAuthentication = existsPhoneAuthenticatedFor10Minute(featureType, phoneNumber, MessageAuthStatus.SEND)
                .orElseThrow(() -> new NotFoundException("인증 번호 발송이 확인되지 않습니다."));

        if (!phoneAuthentication.getPhoneToken().equals(request.getPhoneToken())) {
            throw new NotAcceptableException("인증 번호가 일치하지 않습니다.");
        }
        phoneAuthentication.setStatus(2);
        phoneAuthenticationRepository.save(phoneAuthentication);
    }

    public MemberFullDto info(String header) {
        String token = header.substring(7);
        String email = JwtUtil.parseToken(token);

        return memberRepository.findByEmail(email)
                .map(Member::toMemberFullDto).orElse(null);
    }

    protected void updateLoginAt(Member member) {
        member.setSignInAt(LocalDateTime.now());
        memberRepository.save(member);
    }

    public MemberSearchDto search(SearchRequest request) {
        IdentifierType identifierType = IdentifierType.valueOf(request.getIdentifierType().toUpperCase());
        Optional<Member> member = Optional.empty();
        switch (identifierType) {
            case PHONE:
                member = memberRepository.findByPhoneNumber(StringFilter.onlyNumber(request.getKeyword()));
                break;
            case EMAIL:
                member = memberRepository.findByEmail(request.getKeyword());
                break;
            case NICK:
                member = memberRepository.findByNickName(request.getKeyword());
                break;
        }
        return member.map(Member::toMemberSearchDto).orElse(null);
    }

    @Transactional
    public Boolean sendPhoneToken(PhoneTokenRequest request) {
        FeatureType featureType = FeatureType.valueOf(request.getFeature());
        String phoneNumber = StringFilter.onlyNumber(request.getPhoneNumber());

        LocalDateTime before10Minute = LocalDateTime.now().minusMinutes(10);
        List<PhoneAuthentication> phoneAuthenticationList = phoneAuthenticationRepository
                .findAllByFeatureAndPhoneNumberAndStatusAfterAndCreatedAtAfterOrderByIdDesc(featureType.name(), phoneNumber, MessageAuthStatus.NEW.getValue(), before10Minute);

        if (phoneAuthenticationList.size() > 3) { // 10분이내 4회 까지만 가능
            return false;
        }

        phoneService.sendMessage(featureType.name(), StringFilter.createNumberToken(6), phoneNumber);
        return true;
    }

    public MemberDto verifyPhoneTokenWithChangePassword(ChangePasswordRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("사용자가 없습니다."));

        existsPhoneAuthenticatedFor10Minute(FeatureType.RESET_PASSWORD, member.getPhoneNumber(), MessageAuthStatus.USED)
                .orElseThrow(() -> new NotAcceptableException("전화번호 인증을 먼저 받아야 합니다."));

        member.setPassword(passwordEncoder.encode(request.getPassword()));
        return memberRepository.save(member).toMemberDto();
    }

    private Optional<PhoneAuthentication> existsPhoneAuthenticatedFor10Minute(FeatureType type,
                                                                              String phoneNumber,
                                                                              MessageAuthStatus authStatus) {
        LocalDateTime before10Minute = LocalDateTime.now().minusMinutes(10);
        return phoneAuthenticationRepository.findTopByFeatureAndPhoneNumberAndStatusAndCreatedAtAfterOrderByIdDesc(type.name(),
                        phoneNumber, authStatus.getValue(), before10Minute);
    }
}
