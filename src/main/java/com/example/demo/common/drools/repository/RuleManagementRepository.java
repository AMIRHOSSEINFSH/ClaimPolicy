package com.example.demo.common.drools.repository;

import com.example.demo.common.drools.RuleSearchCriteria;
import com.example.demo.common.drools.entity.BusinessRule;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RuleManagementRepository {
    
    /**
     * Find rules that belong to ALL specified categories (intersection)
     */
    List<BusinessRule> findRulesInAllCategories(Set<String> categoryCodes);
    
    /**
     * Find rules with complex criteria
     */
    List<BusinessRule> findRulesWithCriteria(RuleSearchCriteria criteria);
    
    /**
     * Bulk update rule categories
     */
    void updateRuleCategories(UUID ruleId, Set<UUID> categoryIds);
    
    /**
     * Get category statistics
     */
    Map<String, Long> getCategoryStatistics();
    
    /**
     * Clone a rule with new key
     */
    BusinessRule cloneRule(UUID sourceRuleId, String newRuleKey);
}
