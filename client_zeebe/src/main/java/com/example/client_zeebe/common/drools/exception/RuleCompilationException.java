package com.example.client_zeebe.common.drools.exception;

public class RuleCompilationException extends RuntimeException {
    public RuleCompilationException(String message) {
        super(message);
    }
}