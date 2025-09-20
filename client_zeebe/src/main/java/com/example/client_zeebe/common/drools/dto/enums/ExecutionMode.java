package com.example.client_zeebe.common.drools.dto.enums;

public enum ExecutionMode {
    STRICT,      // Execute exactly as defined
    ADAPTIVE,    // Adapt based on runtime conditions
    OPTIMIZED,   // Optimize for performance
    DEBUG        // Include detailed debug information
}