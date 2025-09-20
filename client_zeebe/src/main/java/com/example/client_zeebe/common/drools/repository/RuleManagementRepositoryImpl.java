package com.example.client_zeebe.common.drools.repository;

import com.example.client_zeebe.common.drools.RuleSearchCriteria;
import com.example.client_zeebe.common.drools.entity.BusinessRule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Transactional
class RuleManagementRepositoryImpl implements RuleManagementRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<BusinessRule> findRulesInAllCategories(Set<String> categoryCodes) {
        if (categoryCodes == null || categoryCodes.isEmpty()) {
            return Collections.emptyList();
        }
        
        String jpql = "SELECT DISTINCT r FROM BusinessRule r " +
                     "JOIN r.categories c " +
                     "WHERE c.categoryCode IN :codes " +
                     "GROUP BY r " +
                     "HAVING COUNT(DISTINCT c.categoryCode) = :count";
        
        return entityManager.createQuery(jpql, BusinessRule.class)
            .setParameter("codes", categoryCodes)
            .setParameter("count", (long) categoryCodes.size())
            .getResultList();
    }
    
    @Override
    public List<BusinessRule> findRulesWithCriteria(RuleSearchCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BusinessRule> query = cb.createQuery(BusinessRule.class);
        Root<BusinessRule> rule = query.from(BusinessRule.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        if (criteria.getRuleKey() != null) {
            predicates.add(cb.like(rule.get("ruleKey"), "%" + criteria.getRuleKey() + "%"));
        }
        
        if (criteria.getRuleName() != null) {
            predicates.add(cb.like(rule.get("ruleName"), "%" + criteria.getRuleName() + "%"));
        }
        
        if (criteria.getRuleType() != null) {
            predicates.add(cb.equal(rule.get("ruleType"), criteria.getRuleType()));
        }
        
        if (criteria.isActiveOnly()) {
            predicates.add(cb.isTrue(rule.get("active")));
        }
        
        if (criteria.getMinPriority() != null) {
            predicates.add(cb.greaterThanOrEqualTo(rule.get("priority"), criteria.getMinPriority()));
        }
        
        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        
        // Apply sorting
        if (criteria.getSortBy() != null) {
            if (criteria.isAscending()) {
                query.orderBy(cb.asc(rule.get(criteria.getSortBy())));
            } else {
                query.orderBy(cb.desc(rule.get(criteria.getSortBy())));
            }
        }
        
        TypedQuery<BusinessRule> typedQuery = entityManager.createQuery(query);
        
        // Apply pagination
        if (criteria.getOffset() != null) {
            typedQuery.setFirstResult(criteria.getOffset());
        }
//        if (criteria.getLimit() != null) {
//            typedQuery.setMaxResults(criteria.getLimit());
//        }
//
        return typedQuery.getResultList();
    }
    
    @Override
    public void updateRuleCategories(UUID ruleId, Set<UUID> categoryIds) {
        // First, delete existing mappings
        entityManager.createNativeQuery(
            "DELETE FROM rule_category_mappings WHERE rule_id = :ruleId")
            .setParameter("ruleId", ruleId)
            .executeUpdate();
        
        // Then, insert new mappings
        for (UUID categoryId : categoryIds) {
            entityManager.createNativeQuery(
                "INSERT INTO rule_category_mappings (rule_id, category_id) VALUES (:ruleId, :categoryId)")
                .setParameter("ruleId", ruleId)
                .setParameter("categoryId", categoryId)
                .executeUpdate();
        }
    }
    
    @Override
    public Map<String, Long> getCategoryStatistics() {
        List<Object[]> results = entityManager.createQuery(
            "SELECT c.categoryCode, COUNT(r) FROM RuleCategory c " +
            "LEFT JOIN c.rules r " +
            "GROUP BY c.categoryCode", Object[].class)
            .getResultList();
        
        Map<String, Long> statistics = new HashMap<>();
        for (Object[] result : results) {
            statistics.put((String) result[0], (Long) result[1]);
        }
        return statistics;
    }
    
    @Override
    public BusinessRule cloneRule(UUID sourceRuleId, String newRuleKey) {
        BusinessRule source = entityManager.find(BusinessRule.class, sourceRuleId);
        if (source == null) {
            throw new EntityNotFoundException("Rule not found: " + sourceRuleId);
        }
        BusinessRule clone = new BusinessRule();
//        clone.setRuleKey(source.getRuleKey());
        clone.setRuleName(source.getRuleName() + " (Copy)");
        clone.setRuleType(source.getRuleType());
        clone.setRuleContent(source.getRuleContent());
        clone.setDescription(source.getDescription());
        clone.setAgendaGroup(source.getAgendaGroup());
        clone.setPriority(source.getPriority());
        clone.setActive(false);
        clone.setTags(new HashSet<>(source.getTags()));

//
//        BusinessRule clone = BusinessRule.builder()
//            .ruleKey(newRuleKey)
//            .ruleName(source.getRuleName() + " (Copy)")
//            .ruleContent(source.getRuleContent())
//            .description(source.getDescription())
//            .ruleType(source.getRuleType())
//            .agendaGroup(source.getAgendaGroup())
//            .priority(source.getPriority())
//            .active(false)  // Cloned rule starts as inactive
//            .metadata(new HashMap<>(source.getMetadata()))
//            .tags(new HashSet<>(source.getTags()))
//            .build();
        
        entityManager.persist(clone);
        
        // Copy category associations
//        for (RuleCategory category : source.getCategories()) {
//            clone.addCategory(category);
//        }
        
        return clone;
    }
}
