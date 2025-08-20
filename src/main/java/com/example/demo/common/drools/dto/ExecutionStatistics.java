package com.example.demo.common.drools.dto;

import com.example.demo.common.drools.enums.ExecutionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ExecutionStatistics {
    private Long totalExecutions;
    private Long successfulExecutions;
    private Long failedExecutions;
    private Double averageExecutionTimeMs;
    private Long maxExecutionTimeMs;
    private Long minExecutionTimeMs;
    private Map<String, Long> executionsByCategory;
    private Map<ExecutionStatus, Long> executionsByStatus;
    private List<HourlyExecutionCount> hourlyDistribution;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;

    public Long getTotalExecutions() {
        return totalExecutions;
    }

    public void setTotalExecutions(Long totalExecutions) {
        this.totalExecutions = totalExecutions;
    }

    public Long getSuccessfulExecutions() {
        return successfulExecutions;
    }

    public void setSuccessfulExecutions(Long successfulExecutions) {
        this.successfulExecutions = successfulExecutions;
    }

    public Long getFailedExecutions() {
        return failedExecutions;
    }

    public void setFailedExecutions(Long failedExecutions) {
        this.failedExecutions = failedExecutions;
    }

    public Double getAverageExecutionTimeMs() {
        return averageExecutionTimeMs;
    }

    public void setAverageExecutionTimeMs(Double averageExecutionTimeMs) {
        this.averageExecutionTimeMs = averageExecutionTimeMs;
    }

    public Long getMaxExecutionTimeMs() {
        return maxExecutionTimeMs;
    }

    public void setMaxExecutionTimeMs(Long maxExecutionTimeMs) {
        this.maxExecutionTimeMs = maxExecutionTimeMs;
    }

    public Long getMinExecutionTimeMs() {
        return minExecutionTimeMs;
    }

    public void setMinExecutionTimeMs(Long minExecutionTimeMs) {
        this.minExecutionTimeMs = minExecutionTimeMs;
    }

    public Map<String, Long> getExecutionsByCategory() {
        return executionsByCategory;
    }

    public void setExecutionsByCategory(Map<String, Long> executionsByCategory) {
        this.executionsByCategory = executionsByCategory;
    }

    public Map<ExecutionStatus, Long> getExecutionsByStatus() {
        return executionsByStatus;
    }

    public void setExecutionsByStatus(Map<ExecutionStatus, Long> executionsByStatus) {
        this.executionsByStatus = executionsByStatus;
    }

    public List<HourlyExecutionCount> getHourlyDistribution() {
        return hourlyDistribution;
    }

    public void setHourlyDistribution(List<HourlyExecutionCount> hourlyDistribution) {
        this.hourlyDistribution = hourlyDistribution;
    }

    public LocalDateTime getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDateTime periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDateTime getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDateTime periodEnd) {
        this.periodEnd = periodEnd;
    }
}