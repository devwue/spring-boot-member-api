package com.devwue.member.model.enums;

public enum ResultStatus {
    SUCCESS("success"),
    FAIL("fail");

    private final String value;

    ResultStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
