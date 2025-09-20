package com.example.client_zeebe.common.drools.dto;

import com.example.client_zeebe.common.drools.enums.ExecutionStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class ExecutionLogDto {
    private UUID id;
    private String processInstanceId;
    private String ruleKey;
    private String planName;
    private Set<String> categoriesExecuted;
    private Integer rulesFired;
    private Long executionTimeMs;
    private ExecutionStatus status;
    private String errorMessage;
    private LocalDateTime executedAt;
    private String executedBy;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getRuleKey() {
        return ruleKey;
    }

    public void setRuleKey(String ruleKey) {
        this.ruleKey = ruleKey;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }

    public String getExecutedBy() {
        return executedBy;
    }

    public void setExecutedBy(String executedBy) {
        this.executedBy = executedBy;
    }
}