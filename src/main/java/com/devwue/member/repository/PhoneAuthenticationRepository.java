package com.devwue.member.repository;

import com.devwue.member.model.entity.PhoneAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneAuthenticationRepository extends JpaRepository<PhoneAuthentication, Long> {
    List<PhoneAuthentication> findAllByEmailAndPhoneNumberAndStatusAfterAndCreatedAtAfterOrderByIdDesc(String email, String phoneNumber, Integer status, LocalDateTime after);
    Optional<PhoneAuthentication> findTopByEmailAndPhoneNumberAndStatusOrderByIdDesc(String email, String phoneNumber, Integer status);

}
