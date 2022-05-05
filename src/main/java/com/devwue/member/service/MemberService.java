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
    private final PhoneService phoneService;

    public MemberService(MemberRepository memberRepository,
                         PasswordEncoder passwordEncoder,
                         PhoneService phoneService) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.phoneService = phoneService;
    }

    @Transactional
    public MemberDto signUp(SignUpRequest request) {
        final String phoneNumber = StringFilter.onlyNumber(request.getPhoneNumber());

        phoneService.getPhoneAuthentication(FeatureType.SIGN_UP, phoneNumber, MessageAuthStatus.USED)
                .orElseThrow(() -> new NotFoundException("needPhoneAuth"));

        final Optional<Member> emailMember = memberRepository.findByEmail(request.getEmail());
        if (emailMember.isPresent()) {
            throw new DuplicationException("foundEmail");
        }

        final Optional<Member> nickMember = memberRepository.findByNickName(request.getNickName());
        if (nickMember.isPresent()) {
            throw new DuplicationException("foundNickName");
        }

        final Optional<Member> phoneMember = memberRepository.findByPhoneNumber(phoneNumber);
        if (phoneMember.isPresent()) {
            throw new DuplicationException("foundPhoneNumber");
        }

        final Member member = new Member().toMember(request);
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        return memberRepository.save(member)
                .toMemberDto();
    }

    public MemberTokenDto login(SignInRequest request) {
        final Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("memberNotFound"));

        boolean matched = passwordEncoder.matches(request.getPassword(), member.getPassword());
        if (!matched) {
            throw new AuthException("missMatchPassword");
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

        PhoneAuthentication phoneAuthentication = phoneService.getPhoneAuthentication(featureType, phoneNumber, MessageAuthStatus.SEND)
                .orElseThrow(() -> new NotFoundException("phoneAuthNotFound"));

        if (!phoneAuthentication.getPhoneToken().equals(request.getPhoneToken())) {
            throw new NotAcceptableException("misMatchPhoneAuth");
        }
        phoneService.updateStatus(phoneAuthentication, MessageAuthStatus.USED);
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

        List<PhoneAuthentication> phoneAuthenticationList = phoneService.getAvailableHistory(featureType, phoneNumber, MessageAuthStatus.NEW);

        if (phoneAuthenticationList.size() > 3) { // 10분이내 4회 까지만 가능
            return false;
        }

        phoneService.sendMessage(featureType, phoneNumber);
        return true;
    }

    public MemberDto verifyPhoneTokenWithChangePassword(ChangePasswordRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("memberNotFound"));

        phoneService.getPhoneAuthentication(FeatureType.RESET_PASSWORD, member.getPhoneNumber(), MessageAuthStatus.USED)
                .orElseThrow(() -> new NotAcceptableException("needPhoneAuth"));

        member.setPassword(passwordEncoder.encode(request.getPassword()));
        return memberRepository.save(member).toMemberDto();
    }
}
