package com.example.clientZeebe.common.drools.dto;

import com.example.clientZeebe.common.drools.enums.ExecutionStrategy;
import com.example.clientZeebe.common.drools.enums.FlowAction;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UpdatePhaseRequest {
    private UUID id;
    private String phaseName;
    private Integer sequenceOrder;
    private Set<String> categoryCodes;
    private ExecutionStrategy executionStrategy;
    private String conditionExpression;
    private FlowAction onSuccessAction;
    private FlowAction onFailureAction;
    private Long maxExecutionTimeMs;
    private Map<String, Object> configuration;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public Integer getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(Integer sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public Set<String> getCategoryCodes() {
        return categoryCodes;
    }

    public void setCategoryCodes(Set<String> categoryCodes) {
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
