package com.devwue.member.service;

import com.devwue.member.model.AuthUser;
import com.devwue.member.model.entity.Member;
import com.devwue.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("### loadUserByUsername -- {}", email);
        final Optional<Member> memberBox = memberRepository.findByEmail(email);
        if (memberBox.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        final Member member = memberBox.get();
        return new AuthUser(member.getEmail(), member.getPassword(), Collections.emptyList());
    }

}
