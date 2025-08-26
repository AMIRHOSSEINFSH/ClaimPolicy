package com.example.demo.common.drools.repository;

import com.example.demo.common.drools.entity.BusinessRule;
import com.example.demo.common.drools.enums.RuleType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface BusinessRuleRepository extends JpaRepository<BusinessRule, UUID>,
        JpaSpecificationExecutor<BusinessRule> {
    
    Optional<BusinessRule> findByRuleName(String ruleKey);

    @Transactional
    @Query("select rule from BusinessRule rule where rule.ruleName in :rules")
    List<BusinessRule> findByRuleNameIn(@Param("rules") Set<String> rules);
    
    List<BusinessRule> findByActiveTrue();
    
    List<BusinessRule> findByActiveTrueAndRuleType(RuleType ruleType);
    
//    @Query("SELECT DISTINCT r FROM BusinessRule r " +
//           "WHERE r.categories = :categoryCode AND r.active = true")
//    List<BusinessRule> findActiveRulesByCategory(@Param("categoryCode") String categoryCode);
    
//    @Query("SELECT DISTINCT r FROM BusinessRule r " +
//           "WHERE r.categories IN :categoryCodes AND r.active = true")
//    List<BusinessRule> findActiveRulesByCategoriesIn(@Param("categoryCodes") Set<String> categoryCodes);
    
//    @Query("SELECT DISTINCT r FROM BusinessRule r " +
//           "WHERE c.id IN :categoryIds AND r.active = true " +
//           "ORDER BY r.priority DESC, r.ruleName ASC")
//    List<BusinessRule> findActiveRulesByCategoryIds(@Param("categoryIds") Set<UUID> categoryIds);
    
    @Query("SELECT r FROM BusinessRule r WHERE r.agendaGroup = :agendaGroup AND r.active = true")
    List<BusinessRule> findByAgendaGroup(@Param("agendaGroup") String agendaGroup);
    
    @Query("SELECT r FROM BusinessRule r WHERE :tag MEMBER OF r.tags AND r.active = true")
    List<BusinessRule> findByTag(@Param("tag") String tag);

    @Query("SELECT DISTINCT r FROM BusinessRule r JOIN r.tags t WHERE t IN :tags AND r.active = true")
    List<BusinessRule> findByTagsIn(@Param("tags") Set<String> tags);

//    @Query("SELECT COUNT(r) FROM BusinessRule r JOIN r.categories c WHERE c.id = :categoryId")
//    Long countRulesByCategory(@Param("categoryId") UUID categoryId);

    @Modifying
    @Query("UPDATE BusinessRule r SET r.active = false WHERE r.id IN :ids")
    void deactivateRules(@Param("ids") Set<UUID> ids);
    
    @Modifying
    @Query("UPDATE BusinessRule r SET r.active = true WHERE r.id IN :ids")
    void activateRules(@Param("ids") Set<UUID> ids);
    
    Page<BusinessRule> findByRuleNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        String name, String description, Pageable pageable);
}