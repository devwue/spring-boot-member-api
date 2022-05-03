package com.devwue.member;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class MemberApplication {
    @Value("${server.time-zone}")
    private String serverTimeZone;

    public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class, args);
    }

    @PostConstruct
    public void atFirst() {
        TimeZone.setDefault(TimeZone.getTimeZone(serverTimeZone));
        System.out.println("Application TimeZone: " + TimeZone.getDefault().toZoneId().toString());
    }
}
