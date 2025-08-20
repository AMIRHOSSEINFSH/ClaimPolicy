package com.example.demo.common.drools.dto;

import com.example.demo.common.drools.enums.RuleType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.Map;
import java.util.Set;

public class UpdateRuleRequest {
    
    @Size(min = 3, max = 255)
    private String ruleName;
    
    private String ruleContent;
    
    private String description;
    
    private RuleType ruleType;
    
    private String agendaGroup;
    
    private String ruleflowGroup;
    
    private String activationGroup;
    
    @Min(0)
    @Max(1000)
    private Integer priority;
    
    private Set<String> categoryCodes;
    
    private Set<String> tags;
    
    private Map<String, Object> metadata;
    
    private Boolean active;

    public @Size(min = 3, max = 255) String getRuleName() {
        return ruleName;
    }

    public void setRuleName(@Size(min = 3, max = 255) String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleContent() {
        return ruleContent;
    }

    public void setRuleContent(String ruleContent) {
        this.ruleContent = ruleContent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public String getAgendaGroup() {
        return agendaGroup;
    }

    public void setAgendaGroup(String agendaGroup) {
        this.agendaGroup = agendaGroup;
    }

    public String getRuleflowGroup() {
        return ruleflowGroup;
    }

    public void setRuleflowGroup(String ruleflowGroup) {
        this.ruleflowGroup = ruleflowGroup;
    }

    public String getActivationGroup() {
        return activationGroup;
    }

    public void setActivationGroup(String activationGroup) {
        this.activationGroup = activationGroup;
    }

    public @Min(0) @Max(1000) Integer getPriority() {
        return priority;
    }

    public void setPriority(@Min(0) @Max(1000) Integer priority) {
        this.priority = priority;
    }

    public Set<String> getCategoryCodes() {
        return categoryCodes;
    }

    public void setCategoryCodes(Set<String> categoryCodes) {
        this.categoryCodes = categoryCodes;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
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