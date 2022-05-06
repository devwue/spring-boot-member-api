package com.devwue.member.service;

import com.devwue.member.exception.NotFoundException;
import com.devwue.member.model.entity.PhoneAuthentication;
import com.devwue.member.model.enums.FeatureType;
import com.devwue.member.model.enums.MessageAuthStatus;
import com.devwue.member.repository.PhoneAuthenticationRepository;
import com.devwue.member.support.StringFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PhoneService {
    private final PhoneAuthenticationRepository phoneAuthenticationRepository;

    public PhoneService(PhoneAuthenticationRepository phoneAuthenticationRepository) {
        this.phoneAuthenticationRepository = phoneAuthenticationRepository;
    }

    public Optional<PhoneAuthentication> getPhoneAuthentication(FeatureType type,
                                                                String phoneNumber,
                                                                MessageAuthStatus authStatus) {
        return phoneAuthenticationRepository
                .findTopByFeatureAndPhoneNumberAndStatusAndCreatedAtAfterOrderByIdDesc(type.name(),
                                                                                    phoneNumber,
                                                                                    authStatus.getValue(),
                                                                                    getDateTimeAfterTimeout());
    }

    public List<PhoneAuthentication> getAvailableHistory(FeatureType type, String phoneNumber, MessageAuthStatus authStatus) {
        return phoneAuthenticationRepository
                .findAllByFeatureAndPhoneNumberAndStatusAfterAndCreatedAtAfterOrderByIdDesc(type.name(),
                                                                                            phoneNumber,
                                                                                            authStatus.getValue(),
                                                                                            getDateTimeAfterTimeout());
    }

    public PhoneAuthentication updateStatus(PhoneAuthentication phoneAuthentication, MessageAuthStatus authStatus) {
        phoneAuthentication.setStatus(authStatus.getValue());
        return phoneAuthenticationRepository.save(phoneAuthentication);
    }

    private PhoneAuthentication newAuthentication(FeatureType type, String phoneNumber, String message) {
        PhoneAuthentication authentication = new PhoneAuthentication()
                .setFeature(type.name())
                .setPhoneNumber(phoneNumber)
                .setPhoneToken(message)
                .setStatus(MessageAuthStatus.NEW.getValue());
        return phoneAuthenticationRepository.saveAndFlush(authentication);
    }

    private LocalDateTime getDateTimeAfterTimeout() {
        return LocalDateTime.now().minusMinutes(10);
    }

    public String getAuthString(String feature, String phoneNumber) {
        PhoneAuthentication authentication = getPhoneAuthentication(FeatureType.valueOf(feature), StringFilter.onlyNumber(phoneNumber), MessageAuthStatus.SEND)
                .orElseThrow(() -> new NotFoundException("phoneAuthNotFound"));
        return authentication.getPhoneToken();
    }

    @Async
    public void sendMessage(FeatureType type, String phoneNumber) {
        PhoneAuthentication authentication = newAuthentication(type, phoneNumber, StringFilter.createNumberToken(6));
        try {
            // todo: external service implement...
            Thread.sleep(5_000);

            log.debug("{} send complete2: {} - {}", Thread.currentThread(), type, phoneNumber);
            authentication.setStatus(MessageAuthStatus.SEND.getValue());
        } catch (InterruptedException e) {
            authentication.setStatus(MessageAuthStatus.FAIL.getValue());
            Thread.interrupted();
        }
        phoneAuthenticationRepository.save(authentication);
    }
}
