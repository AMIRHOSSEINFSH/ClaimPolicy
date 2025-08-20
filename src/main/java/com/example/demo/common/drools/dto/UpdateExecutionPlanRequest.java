package com.example.demo.common.drools.dto;

import com.example.demo.common.drools.enums.PlanType;

import java.util.List;
import java.util.Map;

public class UpdateExecutionPlanRequest {
    private String planName;
    private String description;
    private PlanType planType;
    private List<UpdatePhaseRequest> phases;
    private Map<String, Object> metadata;
    private Boolean active;

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
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

    public List<UpdatePhaseRequest> getPhases() {
        return phases;
    }

    public void setPhases(List<UpdatePhaseRequest> phases) {
        this.phases = phases;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
