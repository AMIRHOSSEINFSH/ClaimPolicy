package com.example.client_zeebe.common.exceptions;

public class RuleExecutionTypeNotDefinedException extends RuntimeException {

    public RuleExecutionTypeNotDefinedException(String message) {
        super(message);
    }
}
