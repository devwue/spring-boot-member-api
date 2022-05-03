package com.devwue.member.repository;

import com.devwue.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickName(String nickName);
    Optional<Member> findByPhoneNumber(String phoneNumber);
    Optional<Member> findByEmailAndPhoneNumber(String email, String phoneNumber);
}
