package com.example.clientZeebe.common.drools.entity;
import com.example.clientZeebe.common.drools.JsonbConverter;
import com.example.clientZeebe.common.drools.enums.ExecutionStrategy;
import com.example.clientZeebe.common.drools.enums.FlowAction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;

import java.util.*;
@Entity
@Table(name = "execution_phases")
public class ExecutionPhase {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    
    @Column(name = "phase_name", nullable = false)
    @NotBlank(message = "Phase name is required")
    private String phaseName;
    
    @Column(name = "sequence_order")
    private Integer sequenceOrder;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "execution_plan_id")
//    @ToString.Exclude
    private ExecutionPlan executionPlan;
    
    @ManyToMany
    @JoinTable(
        name = "phase_category_mappings",
        joinColumns = @JoinColumn(name = "phase_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<RuleCategory> categories = new HashSet<>();
    
    @Column(name = "execution_strategy")
    @Enumerated(EnumType.STRING)
    private ExecutionStrategy executionStrategy = ExecutionStrategy.SEQUENTIAL;
    
    @Column(name = "condition_expression", columnDefinition = "TEXT")
    private String conditionExpression;
    
    @Column(name = "on_success_action")
    @Enumerated(EnumType.STRING)
    private FlowAction onSuccessAction = FlowAction.CONTINUE;
    
    @Column(name = "on_failure_action")
    @Enumerated(EnumType.STRING)
    private FlowAction onFailureAction = FlowAction.STOP;
    
    @Column(name = "max_execution_time_ms")
    private Long maxExecutionTimeMs;
    
    @Column(name = "configuration", columnDefinition = "json")
    @Convert(converter = JsonbConverter.class)
    private Map<String, Object> configuration = new HashMap<>();

    public ExecutionPhase() {
    }

    public ExecutionPhase(UUID id, String phaseName, Integer sequenceOrder, ExecutionPlan executionPlan, Set<RuleCategory> categories, ExecutionStrategy executionStrategy, String conditionExpression, FlowAction onSuccessAction, FlowAction onFailureAction, Long maxExecutionTimeMs, Map<String, Object> configuration) {
        this.id = id;
        this.phaseName = phaseName;
        this.sequenceOrder = sequenceOrder;
        this.executionPlan = executionPlan;
        this.categories = categories;
        this.executionStrategy = executionStrategy;
        this.conditionExpression = conditionExpression;
        this.onSuccessAction = onSuccessAction;
        this.onFailureAction = onFailureAction;
        this.maxExecutionTimeMs = maxExecutionTimeMs;
        this.configuration = configuration;

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public @NotBlank(message = "Phase name is required") String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(@NotBlank(message = "Phase name is required") String phaseName) {
        this.phaseName = phaseName;
    }

    public Integer getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(Integer sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public ExecutionPlan getExecutionPlan() {
        return executionPlan;
    }

    public void setExecutionPlan(ExecutionPlan executionPlan) {
        this.executionPlan = executionPlan;
    }

    public Set<RuleCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<RuleCategory> categories) {
        this.categories = categories;
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
