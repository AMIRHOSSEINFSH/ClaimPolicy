package com.example.demo.common.drools.dto;

import com.example.demo.common.drools.dto.enums.ExecutionMode;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RuleExecutionRequest {
    
    @NotEmpty(message = "At least one category is required")
    private Set<String> categories;
    
    @NotNull(message = "Facts are required")
    private Map<String, Object> facts;
    
    private String processInstanceId;
    
    private ExecutionMode executionMode = ExecutionMode.STRICT;
    
    private Map<String, Object> configuration = new HashMap<>();

    public @NotEmpty(message = "At least one category is required") Set<String> getCategories() {
        return categories;
    }

    public void setCategories(@NotEmpty(message = "At least one category is required") Set<String> categories) {
        this.categories = categories;
    }

    public @NotNull(message = "Facts are required") Map<String, Object> getFacts() {
        return facts;
    }

    public void setFacts(@NotNull(message = "Facts are required") Map<String, Object> facts) {
        this.facts = facts;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public ExecutionMode getExecutionMode() {
        return executionMode;
    }

    public void setExecutionMode(ExecutionMode executionMode) {
        this.executionMode = executionMode;
    }

    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, Object> configuration) {
        this.configuration = configuration;
    }
}
