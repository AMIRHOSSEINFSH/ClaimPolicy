package com.example.demo.common.drools.dto;

import com.example.demo.common.drools.enums.PlanType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateExecutionPlanRequest {
    
    @NotBlank(message = "Plan name is required")
    private String planName;
    
    private String description;
    
    private PlanType planType = PlanType.SEQUENTIAL;
    
    @NotEmpty(message = "At least one phase is required")
    @Valid
    private List<CreatePhaseRequest> phases;

    private boolean active = true;

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

    public @NotEmpty(message = "At least one phase is required") @Valid List<CreatePhaseRequest> getPhases() {
        return phases;
    }

    public void setPhases(@NotEmpty(message = "At least one phase is required") @Valid List<CreatePhaseRequest> phases) {
        this.phases = phases;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}