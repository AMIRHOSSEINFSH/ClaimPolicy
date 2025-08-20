package com.example.demo.common.drools.entity;
import com.example.demo.common.drools.JsonbConverter;
import com.example.demo.common.drools.enums.PlanType;
import com.example.demo.common.drools.enums.RuleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;
@Entity
@Table(name = "execution_plans")

@EntityListeners(AuditingEntityListener.class)
public class ExecutionPlan {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    
    @Column(name = "plan_name", unique = true, nullable = false)
    @NotBlank(message = "Plan name is required")
    private String planName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "plan_type")
    @Enumerated(EnumType.STRING)
    private PlanType planType = PlanType.SEQUENTIAL;
    
    @OneToMany(mappedBy = "executionPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sequenceOrder ASC")
    private List<ExecutionPhase> phases = new ArrayList<>();
    
    @Column(name = "is_active")
    private boolean active = true;
    
    @Column(name = "metadata", columnDefinition = "json")
    @Convert(converter = JsonbConverter.class)
    private Map<String, Object> metadata = new HashMap<>();
    
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Helper methods
    public void addPhase(ExecutionPhase phase) {
        phases.add(phase);
        phase.setExecutionPlan(this);
    }
    
    public void removePhase(ExecutionPhase phase) {
        phases.remove(phase);
        phase.setExecutionPlan(null);
    }

    public ExecutionPlan() {
    }

    public ExecutionPlan(UUID id, String planName, String description, PlanType planType, List<ExecutionPhase> phases, boolean active, Map<String, Object> metadata, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.planName = planName;
        this.description = description;
        this.planType = planType;
        this.phases = phases;
        this.active = active;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public @NotBlank(message = "Plan name is required") String getPlanName() {
        return planName;
    }

    public void setPlanName(@NotBlank(message = "Plan name is required") String planName) {
        this.planName = planName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PlanType getPlanType() {
        return planType;
    }

    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }

    public List<ExecutionPhase> getPhases() {
        return phases;
    }

    public void setPhases(List<ExecutionPhase> phases) {
        this.phases = phases;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}