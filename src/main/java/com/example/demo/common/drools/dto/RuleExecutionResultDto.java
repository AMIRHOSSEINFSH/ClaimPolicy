package com.example.demo.common.drools.dto;

import com.example.demo.common.drools.enums.ExecutionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RuleExecutionResultDto {
    private String executionId;
    private String processInstanceId;
    private Set<String> categoriesExecuted;
    private Integer rulesFired;
    private Map<String, Object> inputData;
    private Map<String, Object> outputData;
    private List<String> executedRules;
    private List<ValidationError> errors;
    private Long executionTimeMs;
    private ExecutionStatus status;
    private LocalDateTime executedAt;

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
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

    public Map<String, Object> getInputData() {
        return inputData;
    }

    public void setInputData(Map<String, Object> inputData) {
        this.inputData = inputData;
    }

    public Map<String, Object> getOutputData() {
        return outputData;
    }

    public void setOutputData(Map<String, Object> outputData) {
        this.outputData = outputData;
    }

    public List<String> getExecutedRules() {
        return executedRules;
    }

    public void setExecutedRules(List<String> executedRules) {
        this.executedRules = executedRules;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
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

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }
}