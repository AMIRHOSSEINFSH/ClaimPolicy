package com.example.clientZeebe.common.drools.dto;

import com.example.clientZeebe.common.drools.enums.ExecutionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PlanExecutionResultDto {
    private String executionId;
    private String planName;
    private List<PhaseExecutionResult> phaseResults;
    private Map<String, Object> finalOutput;
    private Long totalExecutionTimeMs;
    private ExecutionStatus overallStatus;
    private LocalDateTime executedAt;

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public List<PhaseExecutionResult> getPhaseResults() {
        return phaseResults;
    }

    public void setPhaseResults(List<PhaseExecutionResult> phaseResults) {
        this.phaseResults = phaseResults;
    }

    public Map<String, Object> getFinalOutput() {
        return finalOutput;
    }

    public void setFinalOutput(Map<String, Object> finalOutput) {
        this.finalOutput = finalOutput;
    }

    public Long getTotalExecutionTimeMs() {
        return totalExecutionTimeMs;
    }

    public void setTotalExecutionTimeMs(Long totalExecutionTimeMs) {
        this.totalExecutionTimeMs = totalExecutionTimeMs;
    }

    public ExecutionStatus getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(ExecutionStatus overallStatus) {
        this.overallStatus = overallStatus;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }
}
