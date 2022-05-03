package com.devwue.member.service;

import com.devwue.member.controller.model.request.*;
import com.devwue.member.exception.AuthException;
import com.devwue.member.exception.DuplicationException;
import com.devwue.member.exception.NotAcceptableException;
import com.devwue.member.exception.NotFoundException;
import com.devwue.member.model.entity.Member;
import com.devwue.member.model.entity.PhoneAuthentication;
import com.devwue.member.model.enums.IdentifierType;
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
        final Optional<Member> emailMember = memberRepository.findByEmail(request.getEmail());
        if (emailMember.isPresent()) {
            throw new DuplicationException("이미 email 사용자가 있습니다.");
        }

        final Optional<Member> nickMember = memberRepository.findByNickName(request.getNickName());
        if (nickMember.isPresent()) {
            throw new DuplicationException("동일한 닉네임을 사용하는 사용자가 있습니다.");
        }
        String phoneNumber = StringFilter.onlyNumber(request.getPhoneNumber());

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

    public void verifyPhoneNumber(PhoneAuthRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElse(null);
        if (member == null) {
            throw new NotFoundException("계정 정보 조회에 실패 했습니다.");
        }
        String phoneNumber = StringFilter.onlyNumber(request.getPhoneNumber());
        if (!member.getPhoneNumber().equals(phoneNumber)) {
            throw new NotAcceptableException("계정 정보가 일치하지 않습니다.");
        }
        verifyPhoneToken(member.getEmail(), member.getPhoneNumber(), request.getPhoneToken());

        member.setPhoneValidate(true);
        memberRepository.save(member);
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
        String email = request.getEmail();
        String phoneNumber = StringFilter.onlyNumber(request.getPhoneNumber());
        Member member = memberRepository
                .findByEmailAndPhoneNumber(email, phoneNumber).orElse(null);
        if (member == null) {
            return false;
        }

        Integer tokenStatus = 0;
        LocalDateTime before10Minute = LocalDateTime.now().minusMinutes(10);
        List<PhoneAuthentication> phoneAuthenticationList = phoneAuthenticationRepository
                .findAllByEmailAndPhoneNumberAndStatusAfterAndCreatedAtAfterOrderByIdDesc(email, phoneNumber, tokenStatus, before10Minute);

        if (phoneAuthenticationList.size() > 3) { // 10분이내 4회 까지만 가능
            return false;
        }

        PhoneAuthentication authentication = new PhoneAuthentication()
                .setEmail(email)
                .setPhoneNumber(phoneNumber)
                .setPhoneToken(StringFilter.createNumberToken(6))
                .setStatus(0);
        phoneAuthenticationRepository.save(authentication);
        return sendMessageAndSaveResult(authentication);
    }

    public void verifyPhoneToken(String email, String phoneNumber, String phoneToken) {
        Integer tokenStatus = 1;

        PhoneAuthentication phoneAuthentication = phoneAuthenticationRepository
                .findTopByEmailAndPhoneNumberAndStatusOrderByIdDesc(email, phoneNumber, tokenStatus)
                .orElse(null);
        if (phoneAuthentication == null) {
            throw new NotFoundException("인증 번호 발송이 확인되지 않습니다.");
        }
        if (!phoneAuthentication.getPhoneToken().equals(phoneToken)) {
            throw new NotAcceptableException("인증 번호가 일치하지 않습니다.");
        }
        phoneAuthentication.setStatus(2);
    }

    public MemberDto verifyPhoneTokenWithChangePassword(ChangePasswordRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("사용자가 없습니다."));

        verifyPhoneToken(request.getEmail(), member.getPhoneNumber(), request.getPhoneToken());

        member.setPassword(passwordEncoder.encode(request.getPassword()));
        return memberRepository.save(member).toMemberDto();
    }

    private Boolean sendMessageAndSaveResult(PhoneAuthentication authentication) {
        int status = -1;
        boolean result = false;
        try {
            result = phoneService.sendMessage(authentication.getPhoneToken(), authentication.getPhoneNumber());
            if (result) {
                status = 1;
            }
        } catch (Exception e) {
            // always not run
            e.printStackTrace();
        } finally {
            authentication.setStatus(status);
            phoneAuthenticationRepository.save(authentication);
        }
        return result;
    }
}
