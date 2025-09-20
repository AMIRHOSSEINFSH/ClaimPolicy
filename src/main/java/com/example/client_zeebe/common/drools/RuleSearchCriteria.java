package com.example.clientZeebe.common.drools;


import com.example.clientZeebe.common.drools.enums.RuleType;

import java.util.Set;

public class RuleSearchCriteria {
    private String ruleKey;
    private String ruleName;
    private RuleType ruleType;
    private Set<String> categoryCodes;
    private Set<String> tags;
    private Integer minPriority;
    private Integer maxPriority;
    private boolean activeOnly = true;
    private String sortBy = "priority";
    private boolean ascending = false;
    private Integer offset;

    public RuleSearchCriteria() {
    }

    public RuleSearchCriteria(String ruleKey, String ruleName, RuleType ruleType, Set<String> categoryCodes, Set<String> tags, Integer minPriority, Integer maxPriority, boolean activeOnly, String sortBy, boolean ascending, Integer offset) {
        this.ruleKey = ruleKey;
        this.ruleName = ruleName;
        this.ruleType = ruleType;
        this.categoryCodes = categoryCodes;
        this.tags = tags;
        this.minPriority = minPriority;
        this.maxPriority = maxPriority;
        this.activeOnly = activeOnly;
        this.sortBy = sortBy;
        this.ascending = ascending;
        this.offset = offset;
    }

    public String getRuleKey() {
        return ruleKey;
    }

    public void setRuleKey(String ruleKey) {
        this.ruleKey = ruleKey;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
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

    public Integer getMinPriority() {
        return minPriority;
    }

    public void setMinPriority(Integer minPriority) {
        this.minPriority = minPriority;
    }

    public Integer getMaxPriority() {
        return maxPriority;
    }

    public void setMaxPriority(Integer maxPriority) {
        this.maxPriority = maxPriority;
    }

    public boolean isActiveOnly() {
        return activeOnly;
    }

    public void setActiveOnly(boolean activeOnly) {
        this.activeOnly = activeOnly;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}