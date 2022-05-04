package com.devwue.member.service;

import com.devwue.member.model.entity.PhoneAuthentication;
import com.devwue.member.model.enums.MessageAuthStatus;
import com.devwue.member.repository.PhoneAuthenticationRepository;
import com.devwue.member.support.StringFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class PhoneService {
    private final PhoneAuthenticationRepository phoneAuthenticationRepository;

    public PhoneService(PhoneAuthenticationRepository phoneAuthenticationRepository) {
        this.phoneAuthenticationRepository = phoneAuthenticationRepository;
    }

    @Async
    public CompletableFuture<Boolean> sendMessage(String feature, String message, String phoneNumber) {
        PhoneAuthentication authentication = new PhoneAuthentication()
                .setFeature(feature)
                .setPhoneNumber(phoneNumber)
                .setPhoneToken(StringFilter.createNumberToken(6))
                .setStatus(0);
        phoneAuthenticationRepository.save(authentication);

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5_000);
                log.debug("complete: {} - {}", message, phoneNumber);
                authentication.setStatus(MessageAuthStatus.SEND.getValue());
            } catch (InterruptedException e) {
                authentication.setStatus(MessageAuthStatus.FAIL.getValue());
                e.printStackTrace();
                Thread.interrupted();
            }
            phoneAuthenticationRepository.save(authentication);
            return authentication.getStatus() > 0;
        });
    }
}
