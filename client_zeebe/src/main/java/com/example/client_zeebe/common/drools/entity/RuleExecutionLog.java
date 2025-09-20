package com.example.client_zeebe.common.drools.entity;
import com.example.client_zeebe.common.drools.JsonbConverter;
import com.example.client_zeebe.common.drools.enums.ExecutionStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;
@Entity
@Table(name = "rule_execution_logs", indexes = {
    @Index(name = "idx_process_instance", columnList = "process_instance_id"),
    @Index(name = "idx_execution_time", columnList = "executed_at")
})
@EntityListeners(AuditingEntityListener.class)
public class RuleExecutionLog {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    
    @Column(name = "process_instance_id", nullable = false)
    private String processInstanceId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id")
    private BusinessRule rule;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "execution_plan_id")
    private ExecutionPlan executionPlan;
    
    @Column(name = "categories_executed", columnDefinition = "json")
    @Convert(converter = JsonbConverter.class)
    private Set<String> categoriesExecuted = new HashSet<>();
    
    @Column(name = "input_data", columnDefinition = "json")
    @Convert(converter = JsonbConverter.class)
    private Map<String, Object> inputData;
    
    @Column(name = "output_data", columnDefinition = "json")
    @Convert(converter = JsonbConverter.class)
    private Map<String, Object> outputData;
    
    @Column(name = "rules_fired")
    private Integer rulesFired;
    
    @Column(name = "execution_time_ms")
    private Long executionTimeMs;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @CreatedDate
    @Column(name = "executed_at")
    private LocalDateTime executedAt;
    
    @Column(name = "executed_by")
    private String executedBy;

    public RuleExecutionLog() {
    }

    public RuleExecutionLog(UUID id, String processInstanceId, BusinessRule rule, ExecutionPlan executionPlan, Set<String> categoriesExecuted, Map<String, Object> inputData, Map<String, Object> outputData, Integer rulesFired, Long executionTimeMs, ExecutionStatus status, String errorMessage, LocalDateTime executedAt, String executedBy) {
        this.id = id;
        this.processInstanceId = processInstanceId;
        this.rule = rule;
        this.executionPlan = executionPlan;
        this.categoriesExecuted = categoriesExecuted;
        this.inputData = inputData;
        this.outputData = outputData;
        this.rulesFired = rulesFired;
        this.executionTimeMs = executionTimeMs;
        this.status = status;
        this.errorMessage = errorMessage;
        this.executedAt = executedAt;
        this.executedBy = executedBy;
    }

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

    public BusinessRule getRule() {
        return rule;
    }

    public void setRule(BusinessRule rule) {
        this.rule = rule;
    }

    public ExecutionPlan getExecutionPlan() {
        return executionPlan;
    }

    public void setExecutionPlan(ExecutionPlan executionPlan) {
        this.executionPlan = executionPlan;
    }

    public Set<String> getCategoriesExecuted() {
        return categoriesExecuted;
    }

    public void setCategoriesExecuted(Set<String> categoriesExecuted) {
        this.categoriesExecuted = categoriesExecuted;
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
