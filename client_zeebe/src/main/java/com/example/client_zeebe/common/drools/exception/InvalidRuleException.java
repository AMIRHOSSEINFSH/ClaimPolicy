package com.example.client_zeebe.common.drools.exception;

import com.example.client_zeebe.common.drools.dto.ValidationError;

import java.util.List;

public class InvalidRuleException extends RuntimeException {
    private final List<ValidationError> errors;
    
    public InvalidRuleException(String message, List<ValidationError> errors) {
        super(message);
        this.errors = errors;
    }
    
    public List<ValidationError> getErrors() {
        return errors;
    }
}

