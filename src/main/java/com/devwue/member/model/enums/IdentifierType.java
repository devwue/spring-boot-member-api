package com.devwue.member.model.enums;

public enum IdentifierType {
    EMAIL("email"), NICK("nick"), PHONE("phone");

    final String value;

    IdentifierType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
