package com.example.client_zeebe.common.drools.exception;

public class DuplicateRuleKeyException extends RuntimeException {
    public DuplicateRuleKeyException(String message) {
        super(message);
    }
}

