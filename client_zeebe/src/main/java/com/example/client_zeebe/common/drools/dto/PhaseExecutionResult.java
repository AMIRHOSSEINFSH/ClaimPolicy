package com.example.client_zeebe.common.drools.dto;

import com.example.client_zeebe.common.drools.enums.ExecutionStatus;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PhaseExecutionResult {
    private String phaseName;
    private Integer sequenceOrder;
    private Set<String> categoriesExecuted;
    private Integer rulesFired;
    private Map<String, Object> phaseOutput;
    private Long executionTimeMs;
    private ExecutionStatus status;
    private List<String> errors;

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

    public Set<String> getCategoriesExecuted() {
        return categoriesExecuted;
    }

    public void setCategoriesExecuted(Set<String> categoriesExecuted) {
        this.categoriesExecuted = categoriesExecuted;
    }

    public Integer getRulesFired() {
        return rulesFired;
    }

    public void setRulesFired(Integer rulesFired) {
        this.rulesFired = rulesFired;
    }

    public Map<String, Object> getPhaseOutput() {
        return phaseOutput;
    }

    public void setPhaseOutput(Map<String, Object> phaseOutput) {
        this.phaseOutput = phaseOutput;
    }

    public Long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(Long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}