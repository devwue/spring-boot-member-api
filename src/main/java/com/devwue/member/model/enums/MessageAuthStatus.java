package com.devwue.member.model.enums;

public enum MessageAuthStatus {
    FAIL(-1),
    NEW(0),
    SEND(1),
    USED(2),
    EXPIRED( -2);

    private final Integer value;

    MessageAuthStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
