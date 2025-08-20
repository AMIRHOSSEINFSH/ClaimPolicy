package com.example.demo.common.drools.dto;

import com.example.demo.common.drools.enums.RuleType;
import jakarta.validation.constraints.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CreateRuleRequest {
    
    @NotBlank(message = "Rule key is required")
    @Pattern(regexp = "^[A-Z][A-Z0-9_-]*$", message = "Rule key must start with uppercase letter and contain only uppercase letters, numbers, underscore and hyphen")
    private String ruleKey;
    
    @NotBlank(message = "Rule name is required")
    @Size(min = 3, max = 255)
    private String ruleName;
    
    @NotBlank(message = "Rule content is required")
    private String ruleContent;
    
    private String description;
    
    private RuleType ruleType = RuleType.DRL;
    
    private String agendaGroup;
    
    private String ruleflowGroup;
    
    private String activationGroup;
    
    @Min(value = 0, message = "Priority must be non-negative")
    @Max(value = 1000, message = "Priority must not exceed 1000")
    private Integer priority = 0;
    
    @NotEmpty(message = "At least one category is required")
    private Set<String> categoryCodes;
    
    private Set<String> tags = new HashSet<>();
    
    private Map<String, Object> metadata = new HashMap<>();
    
    private boolean active = true;

    public @NotBlank(message = "Rule key is required") @Pattern(regexp = "^[A-Z][A-Z0-9_-]*$", message = "Rule key must start with uppercase letter and contain only uppercase letters, numbers, underscore and hyphen") String getRuleKey() {
        return ruleKey;
    }

    public void setRuleKey(@NotBlank(message = "Rule key is required") @Pattern(regexp = "^[A-Z][A-Z0-9_-]*$", message = "Rule key must start with uppercase letter and contain only uppercase letters, numbers, underscore and hyphen") String ruleKey) {
        this.ruleKey = ruleKey;
    }

    public @NotBlank(message = "Rule name is required") @Size(min = 3, max = 255) String getRuleName() {
        return ruleName;
    }

    public void setRuleName(@NotBlank(message = "Rule name is required") @Size(min = 3, max = 255) String ruleName) {
        this.ruleName = ruleName;
    }

    public @NotBlank(message = "Rule content is required") String getRuleContent() {
        return ruleContent;
    }

    public void setRuleContent(@NotBlank(message = "Rule content is required") String ruleContent) {
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

    public @Min(value = 0, message = "Priority must be non-negative") @Max(value = 1000, message = "Priority must not exceed 1000") Integer getPriority() {
        return priority;
    }

    public void setPriority(@Min(value = 0, message = "Priority must be non-negative") @Max(value = 1000, message = "Priority must not exceed 1000") Integer priority) {
        this.priority = priority;
    }

    public @NotEmpty(message = "At least one category is required") Set<String> getCategoryCodes() {
        return categoryCodes;
    }

    public void setCategoryCodes(@NotEmpty(message = "At least one category is required") Set<String> categoryCodes) {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}