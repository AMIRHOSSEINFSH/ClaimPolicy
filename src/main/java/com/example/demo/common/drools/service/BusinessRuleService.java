package com.example.demo.common.drools.service;


import com.example.demo.common.drools.RuleSearchCriteria;
import com.example.demo.common.drools.dto.*;
import com.example.demo.common.drools.entity.BusinessRule;
import com.example.demo.common.drools.entity.RuleCategory;
import com.example.demo.common.drools.exception.CategoryNotFoundException;
import com.example.demo.common.drools.exception.DuplicateRuleKeyException;
import com.example.demo.common.drools.exception.InvalidRuleException;
import com.example.demo.common.drools.exception.RuleNotFoundException;
import com.example.demo.common.drools.repository.BusinessRuleRepository;
import com.example.demo.common.drools.repository.RuleCategoryRepository;
import com.example.demo.common.drools.repository.RuleManagementRepository;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Service
@Transactional
public class BusinessRuleService {
    
    private final BusinessRuleRepository ruleRepository;
    private final RuleCategoryRepository categoryRepository;
    private final RuleManagementRepository managementRepository;
    private final RuleMapper ruleMapper;
    private final KieServices kieServices;
//    private final CacheManager cacheManager;

    public BusinessRuleService(BusinessRuleRepository ruleRepository, RuleCategoryRepository categoryRepository, @Qualifier(value = "ruleManagementRepositoryImpl") RuleManagementRepository managementRepository, RuleMapper ruleMapper/*, KieServices kieServices*//*, CacheManager cacheManager*/) {
        this.ruleRepository = ruleRepository;
        this.categoryRepository = categoryRepository;
        this.managementRepository = managementRepository;
        this.ruleMapper = ruleMapper;
        this.kieServices = KieServices.Factory.get();
//        this.cacheManager = cacheManager;
    }

    public Page<BusinessRuleDto> getAllRules(PageRequest pageRequest, Boolean active) {
        Page<BusinessRule> rules;
        
        if (active != null) {
            rules = ruleRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("active"), active),
                pageRequest
            );
        } else {
            rules = ruleRepository.findAll(pageRequest);
        }
        
        return rules.map(ruleMapper::toDto);
    }
    
    @Cacheable(value = "rules", key = "#id")
    public Optional<BusinessRuleDto> getRuleById(UUID id) {
        return ruleRepository.findById(id)
                .map(ruleMapper::toDto);
    }
    
    @Cacheable(value = "rules", key = "#ruleKey")
    public Optional<BusinessRuleDto> getRuleByKey(String ruleKey) {
        return ruleRepository.findByRuleKey(ruleKey)
                .map(ruleMapper::toDto);
    }

    public BusinessRuleDto createRule(CreateRuleRequest request) {
        // Validate rule content
        ValidationResult validation = validateRuleContent(request.getRuleContent());
        if (!validation.isValid()) {
            throw new InvalidRuleException("Invalid rule content", validation.getErrors());
        }
        
        // Check if rule key already exists
        if (ruleRepository.findByRuleKey(request.getRuleKey()).isPresent()) {
            throw new DuplicateRuleKeyException("Rule with key " + request.getRuleKey() + " already exists");
        }
        
        // Create rule entity
//        BusinessRule rule = BusinessRule.builder()
//                .ruleKey(request.getRuleKey())
//                .ruleName(request.getRuleName())
//                .ruleContent(request.getRuleContent())
//                .description(request.getDescription())
//                .ruleType(request.getRuleType())
//                .agendaGroup(request.getAgendaGroup())
//                .ruleflowGroup(request.getRuleflowGroup())
//                .activationGroup(request.getActivationGroup())
//                .priority(request.getPriority())
//                .active(request.isActive())
//                .tags(request.getTags())
//                .metadata(request.getMetadata())
//                .build();
        BusinessRule rule = new BusinessRule();

        rule.setRuleKey(request.getRuleKey());
        rule.setRuleName(request.getRuleName());
        rule.setRuleContent(request.getRuleContent());
        rule.setDescription(request.getDescription());
        rule.setRuleType(request.getRuleType());
        rule.setAgendaGroup(request.getAgendaGroup());
        rule.setRuleflowGroup(request.getRuleflowGroup());
        rule.setActivationGroup(request.getActivationGroup());
        rule.setPriority(request.getPriority());
        rule.setActive(request.isActive());
        rule.setTags(request.getTags());
        rule.setMetadata(request.getMetadata());

        // Add categories
        Set<RuleCategory> categories = loadCategories(request.getCategoryCodes());
        for (RuleCategory category : categories) {
            rule.addCategory(category);
        }
        
        BusinessRule saved = ruleRepository.save(rule);
//        log.info("Created rule: {} with categories: {}", saved.getRuleKey(), request.getCategoryCodes());
        
        // Clear cache
//        clearRuleCache();
        
        return ruleMapper.toDto(saved);
    }
    
    @CacheEvict(value = "rules", key = "#id")
    public Optional<BusinessRuleDto> updateRule(UUID id, UpdateRuleRequest request) {
        return ruleRepository.findById(id)
                .map(rule -> {
                    // Validate rule content if provided
                    if (request.getRuleContent() != null) {
                        ValidationResult validation = validateRuleContent(request.getRuleContent());
                        if (!validation.isValid()) {
                            throw new InvalidRuleException("Invalid rule content", validation.getErrors());
                        }
                        rule.setRuleContent(request.getRuleContent());
                    }
                    
                    // Update fields
                    if (request.getRuleName() != null) {
                        rule.setRuleName(request.getRuleName());
                    }
                    if (request.getDescription() != null) {
                        rule.setDescription(request.getDescription());
                    }
                    if (request.getRuleType() != null) {
                        rule.setRuleType(request.getRuleType());
                    }
                    if (request.getAgendaGroup() != null) {
                        rule.setAgendaGroup(request.getAgendaGroup());
                    }
                    if (request.getPriority() != null) {
                        rule.setPriority(request.getPriority());
                    }
                    if (request.getActive() != null) {
                        rule.setActive(request.getActive());
                    }
                    if (request.getTags() != null) {
                        rule.setTags(request.getTags());
                    }
                    if (request.getMetadata() != null) {
                        rule.setMetadata(request.getMetadata());
                    }
                    
                    // Update categories if provided
                    if (request.getCategoryCodes() != null) {
                        updateRuleCategories(rule, request.getCategoryCodes());
                    }
                    
                    BusinessRule updated = ruleRepository.save(rule);
//                    clearRuleCache();
                    
                    return ruleMapper.toDto(updated);
                });
    }
    
    @CacheEvict(value = "rules", key = "#id")
    public void deleteRule(UUID id) {
        if (!ruleRepository.existsById(id)) {
            throw new RuleNotFoundException("Rule not found: " + id);
        }
        
        ruleRepository.deleteById(id);
//        clearRuleCache();
//        log.info("Deleted rule: {}", id);
    }
    
    public Optional<BusinessRuleDto> activateRule(UUID id) {
        return updateRuleStatus(id, true);
    }
    
    public Optional<BusinessRuleDto> deactivateRule(UUID id) {
        return updateRuleStatus(id, false);
    }
    
    private Optional<BusinessRuleDto> updateRuleStatus(UUID id, boolean active) {
        return ruleRepository.findById(id)
                .map(rule -> {
                    rule.setActive(active);
                    BusinessRule updated = ruleRepository.save(rule);
//                    clearRuleCache();
//                    log.info("{} rule: {}", active ? "Activated" : "Deactivated", rule.getRuleKey());
                    return ruleMapper.toDto(updated);
                });
    }
    
    public Optional<BusinessRuleDto> addCategoriesToRule(UUID ruleId, Set<String> categoryCodes) {
        return ruleRepository.findById(ruleId)
                .map(rule -> {
                    Set<RuleCategory> categories = loadCategories(categoryCodes);
                    for (RuleCategory category : categories) {
                        rule.addCategory(category);
                    }
                    BusinessRule updated = ruleRepository.save(rule);
//                    clearRuleCache();
                    return ruleMapper.toDto(updated);
                });
    }
    
    public Optional<BusinessRuleDto> removeCategoriesFromRule(UUID ruleId, Set<String> categoryCodes) {
        return ruleRepository.findById(ruleId)
                .map(rule -> {
                    Set<RuleCategory> categories = loadCategories(categoryCodes);
                    for (RuleCategory category : categories) {
                        rule.removeCategory(category);
                    }
                    BusinessRule updated = ruleRepository.save(rule);
//                    clearRuleCache();
                    return ruleMapper.toDto(updated);
                });
    }
    
    public BusinessRuleDto cloneRule(UUID sourceId, String newRuleKey) {
        BusinessRule cloned = managementRepository.cloneRule(sourceId, newRuleKey);
        return ruleMapper.toDto(cloned);
    }
    
    public List<BusinessRuleDto> searchRules(RuleSearchRequest request) {
//        RuleSearchCriteria criteria = RuleSearchCriteria.builder()
//                .ruleKey(request.getRuleKey())
//                .ruleName(request.getRuleName())
//                .ruleType(request.getRuleType())
//                .categoryCodes(request.getCategoryCodes())
//                .tags(request.getTags())
//                .minPriority(request.getMinPriority())
//                .maxPriority(request.getMaxPriority())
//                .activeOnly(request.getActiveOnly())
//                .sortBy(request.getSortBy())
//                .ascending(request.getAscending())
//                .offset(request.getPage() * request.getSize())
//                .limit(request.getSize())
//                .build();
        RuleSearchCriteria criteria = new RuleSearchCriteria();

        criteria.setRuleKey(request.getRuleKey());
        criteria.setRuleName(request.getRuleName());
        criteria.setRuleType(request.getRuleType());
        criteria.setCategoryCodes(request.getCategoryCodes());
        criteria.setTags(request.getTags());
        criteria.setMinPriority(request.getMinPriority());
        criteria.setMaxPriority(request.getMaxPriority());
        criteria.setActiveOnly(request.getActiveOnly());
        criteria.setSortBy(request.getSortBy());
        criteria.setAscending(request.getAscending());
        criteria.setOffset(request.getPage() * request.getSize());
//        criteria.setLimit(request.getSize());


        List<BusinessRule> rules = managementRepository.findRulesWithCriteria(criteria);
        return rules.stream()
                .map(ruleMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public ValidationResult validateRuleContent(String ruleContent) {
        ValidationResult result = new ValidationResult();
        result.setValid(true);
        
        try {
            // Create a temporary KieFileSystem
            KieFileSystem kfs = kieServices.newKieFileSystem();
            kfs.write("src/main/resources/temp/validation.drl", ruleContent);
            
            // Build and check for errors
            KieBuilder kieBuilder = kieServices.newKieBuilder(kfs);
            Results buildResults = kieBuilder.buildAll().getResults();
            
            if (buildResults.hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
                result.setValid(false);
                buildResults.getMessages(org.kie.api.builder.Message.Level.ERROR)
                        .forEach(msg -> {
//                            ValidationError error = ValidationError.builder()
//                                    .code("DRL_COMPILE_ERROR")
//                                    .message(msg.getText())
//                                    .line(msg.getLine())
//                                    .column(msg.getColumn())
//                                    .build();
                            ValidationError error = new ValidationError();

                            error.setCode("DRL_COMPILE_ERROR");
                            error.setMessage(msg.getText());
                            error.setLine(msg.getLine());
                            error.setColumn(msg.getColumn());

                            result.getErrors().add(error);
                        });
            }
            
            // Add warnings
            buildResults.getMessages(org.kie.api.builder.Message.Level.WARNING)
                    .forEach(msg -> {
//                        ValidationWarning warning = ValidationWarning.builder()
//                                .code("DRL_COMPILE_WARNING")
//                                .message(msg.getText())
//                                .build();
                        ValidationWarning warning = new ValidationWarning();
                        warning.setCode("DRL_COMPILE_WARNING");
                        warning.setMessage(msg.getText());
                        result.getWarnings().add(warning);
                    });
            
        } catch (Exception e) {
            result.setValid(false);
//            ValidationError error = ValidationError.builder()
//                    .code("VALIDATION_ERROR")
//                    .message(e.getMessage())
//                    .build();
            ValidationError error = new ValidationError();
            error.setCode("VALIDATION_ERROR");
            error.setMessage(e.getMessage());
            result.getErrors().add(error);
        }
        
        return result;
    }
    
    private Set<RuleCategory> loadCategories(Set<String> categoryCodes) {
        Set<RuleCategory> categories = new HashSet<>();
        for (String code : categoryCodes) {
            RuleCategory category = categoryRepository.findByCategoryCode(code)
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + code));
            categories.add(category);
        }
        return categories;
    }
    
    private void updateRuleCategories(BusinessRule rule, Set<String> categoryCodes) {
        // Clear existing categories
        rule.getCategories().clear();
        
        // Add new categories
        Set<RuleCategory> categories = loadCategories(categoryCodes);
        for (RuleCategory category : categories) {
            rule.addCategory(category);
        }
    }
    
//    private void clearRuleCache() {
////        cacheManager.getCache("rules").clear();
////        cacheManager.getCache("compiledRules").clear();
//    }
}
