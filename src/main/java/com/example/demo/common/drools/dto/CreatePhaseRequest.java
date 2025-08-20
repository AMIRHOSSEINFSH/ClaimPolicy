package com.example.demo.common.drools.dto;

import com.example.demo.common.drools.enums.ExecutionStrategy;
import com.example.demo.common.drools.enums.FlowAction;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CreatePhaseRequest {
    
    @NotBlank(message = "Phase name is required")
    private String phaseName;
    
    @NotNull(message = "Sequence order is required")
    @Min(1)
    private Integer sequenceOrder;
    
    @NotEmpty(message = "At least one category is required")
    private Set<String> categoryCodes;
    
    private ExecutionStrategy executionStrategy = ExecutionStrategy.SEQUENTIAL;
    
    private String conditionExpression;
    
    private FlowAction onSuccessAction = FlowAction.CONTINUE;
    
    private FlowAction onFailureAction = FlowAction.STOP;
    
    private Long maxExecutionTimeMs;
    
    private Map<String, Object> configuration = new HashMap<>();

    public @NotBlank(message = "Phase name is required") String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(@NotBlank(message = "Phase name is required") String phaseName) {
        this.phaseName = phaseName;
    }

    public @NotNull(message = "Sequence order is required") @Min(1) Integer getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(@NotNull(message = "Sequence order is required") @Min(1) Integer sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public @NotEmpty(message = "At least one category is required") Set<String> getCategoryCodes() {
        return categoryCodes;
    }

    public void setCategoryCodes(@NotEmpty(message = "At least one category is required") Set<String> categoryCodes) {
        this.categoryCodes = categoryCodes;
    }

    public ExecutionStrategy getExecutionStrategy() {
        return executionStrategy;
    }

    public void setExecutionStrategy(ExecutionStrategy executionStrategy) {
        this.executionStrategy = executionStrategy;
    }

    public String getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    public FlowAction getOnSuccessAction() {
        return onSuccessAction;
    }

    public void setOnSuccessAction(FlowAction onSuccessAction) {
        this.onSuccessAction = onSuccessAction;
    }

    public FlowAction getOnFailureAction() {
        return onFailureAction;
    }

    public void setOnFailureAction(FlowAction onFailureAction) {
        this.onFailureAction = onFailureAction;
    }

    public Long getMaxExecutionTimeMs() {
        return maxExecutionTimeMs;
    }

    public void setMaxExecutionTimeMs(Long maxExecutionTimeMs) {
        this.maxExecutionTimeMs = maxExecutionTimeMs;
    }

    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, Object> configuration) {
        this.configuration = configuration;
    }
}