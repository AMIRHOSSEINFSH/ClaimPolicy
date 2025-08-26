package com.example.demo.common.drools.service;


import com.example.demo.common.drools.RuleSearchCriteria;
import com.example.demo.common.drools.dto.*;
import com.example.demo.common.drools.entity.BusinessRule;
import com.example.demo.common.drools.entity.RuleCategory;
import com.example.demo.common.drools.enums.ExecutionType;
import com.example.demo.common.drools.exception.CategoryNotFoundException;
import com.example.demo.common.drools.exception.DuplicateRuleKeyException;
import com.example.demo.common.drools.exception.InvalidRuleException;
import com.example.demo.common.drools.exception.RuleNotFoundException;
import com.example.demo.common.drools.repository.BusinessRuleRepository;
import com.example.demo.common.drools.repository.RuleCategoryRepository;
import com.example.demo.common.drools.repository.RuleManagementRepository;
import org.drools.drl.ast.descr.AnnotationDescr;
import org.drools.drl.ast.descr.PackageDescr;
import org.drools.drl.ast.descr.RuleDescr;
import org.drools.drl.parser.DrlParser;
import org.drools.drl.parser.DroolsParserException;
import org.drools.mvel.DrlDumper;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Results;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.common.drools.enums.RuleType.DRL;


@Service
@Transactional
public class BusinessRuleService {

    private final BusinessRuleRepository ruleRepository;
    private final RuleCategoryRepository categoryRepository;
    private final RuleManagementRepository managementRepository;
    private final RuleMapper ruleMapper;
    private final KieServices kieServices;

    public BusinessRuleService(BusinessRuleRepository ruleRepository, RuleCategoryRepository categoryRepository, @Qualifier(value = "ruleManagementRepositoryImpl") RuleManagementRepository managementRepository, RuleMapper ruleMapper) {
        this.ruleRepository = ruleRepository;
        this.categoryRepository = categoryRepository;
        this.managementRepository = managementRepository;
        this.ruleMapper = ruleMapper;
        this.kieServices = KieServices.Factory.get();
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
        return ruleRepository.findByRuleName(ruleKey)
                .map(ruleMapper::toDto);
    }

    private String getRuleContentFromFile(MultipartFile drlFile) {
        InputStream inputStream = null;
        String drlContent = null;
        try {
            inputStream = drlFile.getInputStream();
            var byteArray = inputStream.readAllBytes();
            drlContent = new String(byteArray, StandardCharsets.UTF_8);

//            ValidationResult validation = validateRuleContent(drlContent);
//            if (!validation.isValid()) {
//                throw new InvalidRuleException("Invalid rule content", validation.getErrors());
//            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return drlContent;
    }

    public List<BusinessRuleDto> createRule(MultipartFile drlFile) {
        String drlContent = getRuleContentFromFile(drlFile);
        ValidationResult validation = validateRuleContent(drlContent);
        if (!validation.isValid()) {
            throw new InvalidRuleException("Invalid rule content", validation.getErrors());
        }
        DrlParser drlParser = new DrlParser();
        PackageDescr packageDescr = null;
        try {
            packageDescr = drlParser.parse(new StringReader(drlContent));
            if (packageDescr.getRules().size() != 1){
                throw new InvalidRuleException("Invalid rule content", validation.getErrors());
            }

        } catch (DroolsParserException e) {
            throw new RuntimeException(e);
        }
        List<CreateRuleRequest> createRuleRequestList = new ArrayList<>();
        var itRule = packageDescr.getRules().getFirst();
            CreateRuleRequest createRuleRequest = new CreateRuleRequest();
            createRuleRequest.setRawRule(drlContent);
            createRuleRequest.setRuleName(itRule.getName());
            List<String> categoryArray = getCategoryAnnotation(itRule);
            createRuleRequest.setActivationGroup(getInfoAnnotation(itRule,"ACTIVATION_GROUP"));
            createRuleRequest.setActive(true);
            createRuleRequest.setRuleType(DRL);
            StringBuilder sb = new StringBuilder();
            DrlDumper drlDumper = new DrlDumper();
            itRule.getLhs().getDescrs().forEach(item->{
                sb.append(drlDumper.dump(item))
                        .append("\n")
                        .append("    ");
            });
            createRuleRequest.setRuleContent(String.format(tmp, sb,itRule.getConsequence()));
            createRuleRequest.setAgendaGroup(getInfoAnnotation(itRule,"AGENDA_GROUP"));
            createRuleRequest.setPriority(Integer.parseInt(itRule.getSalience() != null ? itRule.getSalience() : "0"));
            if (categoryArray != null)
                createRuleRequest.setCategoryCodes(new HashSet<>(categoryArray));
            createRuleRequestList.add(createRuleRequest);


        return createRule(createRuleRequestList);
    }

    String tmp = "when \n" +
            "    %s\n" +
            "then \n" +
            "    %s\n" +
            " end";

    public List<BusinessRuleDto> createRule(List<CreateRuleRequest> createRuleRequestList) {
        var rules= createRuleRequestList.stream().map(this::mapToRuleEntity).toList();
        List<BusinessRule> saved = ruleRepository.saveAll(rules);

        return saved.stream().map(ruleMapper::toDto).toList();
    }

    public BusinessRule mapToRuleEntity(CreateRuleRequest request) {
        ValidationResult validation = validateRuleContent(request.getRawRule() == null ? request.getRuleContent() : request.getRawRule());
        if (!validation.isValid()) {
            throw new InvalidRuleException("Invalid rule content", validation.getErrors());
        }

        // Check if rule key already exists
        if (ruleRepository.findByRuleName(request.getRuleName()).isPresent()) {
            throw new DuplicateRuleKeyException("Rule with key " + request.getRuleName() + " already exists");
        }

        BusinessRule rule = new BusinessRule();

        rule.setRuleName(request.getRuleName());
        rule.setRuleContent(request.getRawRule());
        rule.setDescription(request.getDescription());
        rule.setRuleType(request.getRuleType());
        rule.setAgendaGroup(request.getAgendaGroup());
        rule.setRuleflowGroup(request.getRuleflowGroup());
        rule.setActivationGroup(request.getActivationGroup());
        rule.setPriority(request.getPriority());
        rule.setActive(request.isActive());
        rule.setTags(request.getTags());
        rule.setCategories(request.getCategoryCodes());

        // Add categories
//        Set<RuleCategory> categories = loadCategories(request.getCategoryCodes());
//        for (RuleCategory category : categories) {
//            rule.addCategory(category);
//        }

        return rule;
    }
    public BusinessRuleDto createRule(CreateRuleRequest request) {
        // Validate rule content
        BusinessRule rule = mapToRuleEntity(request);

        BusinessRule saved = ruleRepository.save(rule);
//        log.info("Created rule: {} with categories: {}", saved.getRuleKey(), request.getCategoryCodes());

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
            kfs.write("src/main/resources/com/example/demo/validation.drl", ruleContent);

            // Build and check for errors
            KieBuilder kieBuilder = kieServices.newKieBuilder(kfs);
            Results buildResults = kieBuilder.buildAll().getResults();

            if (buildResults.hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
                result.setValid(false);
                buildResults.getMessages(org.kie.api.builder.Message.Level.ERROR)
                        .forEach(msg -> {
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
                        ValidationWarning warning = new ValidationWarning();
                        warning.setCode("DRL_COMPILE_WARNING");
                        warning.setMessage(msg.getText());
                        result.getWarnings().add(warning);
                    });

        } catch (Exception e) {
            result.setValid(false);
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

    protected String getInfoAnnotation(RuleDescr ruleDescr, String key) {
        Optional<AnnotationDescr> annotationDescr = ruleDescr.getAnnotations().stream().filter(item-> Objects.equals(item.getName(), "info")).findFirst();

        if (annotationDescr.isPresent()) {
            return annotationDescr.get().getValueAsString(key);
        }

        return null;
    }

    protected List<String> getCategoryAnnotation(RuleDescr ruleDescr) {
        Optional<AnnotationDescr> annotationDescr = ruleDescr.getAnnotations().stream().filter(item-> Objects.equals(item.getName(), "Category")).findFirst();

        if (annotationDescr.isPresent()) {
            Object value = annotationDescr.get().getValue();
            if (value == null) return null;

            String rawCategories = value.toString();
            return Arrays.stream(rawCategories.split(",")).map(String::trim).toList();
        }

        return null;
    }
}
