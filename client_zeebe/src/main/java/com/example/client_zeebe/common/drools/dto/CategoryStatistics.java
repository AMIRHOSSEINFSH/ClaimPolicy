package com.example.client_zeebe.common.drools.dto;

import java.time.LocalDateTime;

public class CategoryStatistics {
    private String categoryCode;
    private String categoryName;
    private Long totalRules;
    private Long activeRules;
    private Long inactiveRules;
    private Long subCategoryCount;
    private LocalDateTime lastRuleAdded;
    private LocalDateTime lastRuleExecuted;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getTotalRules() {
        return totalRules;
    }

    public void setTotalRules(Long totalRules) {
        this.totalRules = totalRules;
    }

    public Long getActiveRules() {
        return activeRules;
    }

    public void setActiveRules(Long activeRules) {
        this.activeRules = activeRules;
    }

    public Long getInactiveRules() {
        return inactiveRules;
    }

    public void setInactiveRules(Long inactiveRules) {
        this.inactiveRules = inactiveRules;
    }

    public Long getSubCategoryCount() {
        return subCategoryCount;
    }

    public void setSubCategoryCount(Long subCategoryCount) {
        this.subCategoryCount = subCategoryCount;
    }

    public LocalDateTime getLastRuleAdded() {
        return lastRuleAdded;
    }

    public void setLastRuleAdded(LocalDateTime lastRuleAdded) {
        this.lastRuleAdded = lastRuleAdded;
    }

    public LocalDateTime getLastRuleExecuted() {
        return lastRuleExecuted;
    }

    public void setLastRuleExecuted(LocalDateTime lastRuleExecuted) {
        this.lastRuleExecuted = lastRuleExecuted;
    }
}