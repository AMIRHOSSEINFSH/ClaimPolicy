package com.example.clientZeebe.common.drools.controller;


import com.example.clientZeebe.common.drools.dto.*;
import com.example.clientZeebe.common.drools.service.BusinessRuleService;
import com.example.clientZeebe.common.drools.service.RuleExecutionService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@RestController
@RequestMapping("/api/v1/rules")
@Tag(name = "Business Rules", description = "Manage business rules with multi-category support")
@Validated
public class BusinessRuleController {
    
    private final BusinessRuleService ruleService;
    private final RuleExecutionService executionService;

    public BusinessRuleController(BusinessRuleService ruleService, RuleExecutionService executionService) {
        this.ruleService = ruleService;
        this.executionService = executionService;
    }

    @GetMapping
    @Operation(summary = "Get all rules", description = "Retrieve all business rules with pagination")
    @ApiResponse(responseCode = "200", description = "Rules retrieved successfully")
    public ResponseEntity<Page<BusinessRuleDto>> getAllRules(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "priority,desc") String sort,
            @RequestParam(required = false) Boolean active) {
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sort)));
        Page<BusinessRuleDto> rules = ruleService.getAllRules(pageRequest, active);
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get rule by ID", description = "Retrieve a specific business rule")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rule found"),
        @ApiResponse(responseCode = "404", description = "Rule not found")
    })
    public ResponseEntity<BusinessRuleDto> getRuleById(@PathVariable UUID id) {
        return ruleService.getRuleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/key/{ruleKey}")
    @Operation(summary = "Get rule by key", description = "Retrieve a rule by its unique key")
    public ResponseEntity<BusinessRuleDto> getRuleByKey(@PathVariable String ruleKey) {
        return ruleService.getRuleByKey(ruleKey)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Create new rule", description = "Create a new business rule with categories")
    @ApiResponse(responseCode = "201", description = "Rule created successfully")
    public ResponseEntity<BusinessRuleDto> createRule(@Valid @RequestBody CreateRuleRequest request) {
        BusinessRuleDto created = ruleService.createRule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping(value = "/drl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Import rules from DRL file",
            description = "Upload a .drl file containing Drools rules"
    )
    @ApiResponse(responseCode = "200", description = "Rules imported successfully")
    @ApiResponse(responseCode = "400", description = "Invalid file format or content")
    public ResponseEntity<List<BusinessRuleDto>> importFromDRL(
            @RequestParam("file") @NotNull MultipartFile file) {

        List<BusinessRuleDto> validation = ruleService.createRule(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(validation);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update rule", description = "Update an existing business rule")
    public ResponseEntity<BusinessRuleDto> updateRule(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateRuleRequest request) {
        
//        log.info("Updating rule: {}", id);
        return ruleService.updateRule(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete rule", description = "Delete a business rule")
    @ApiResponse(responseCode = "204", description = "Rule deleted successfully")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
//        log.info("Deleting rule: {}", id);
        ruleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate rule", description = "Activate a deactivated rule")
    public ResponseEntity<BusinessRuleDto> activateRule(@PathVariable UUID id) {
        return ruleService.activateRule(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate rule", description = "Deactivate an active rule")
    public ResponseEntity<BusinessRuleDto> deactivateRule(@PathVariable UUID id) {
        return ruleService.deactivateRule(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/{id}/categories")
    @Operation(summary = "Add categories to rule", description = "Add multiple categories to a rule")
    public ResponseEntity<BusinessRuleDto> addCategories(
            @PathVariable UUID id,
            @RequestBody @NotEmpty Set<String> categoryCodes) {
        
        return ruleService.addCategoriesToRule(id, categoryCodes)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}/categories")
    @Operation(summary = "Remove categories from rule", description = "Remove categories from a rule")
    public ResponseEntity<BusinessRuleDto> removeCategories(
            @PathVariable UUID id,
            @RequestBody @NotEmpty Set<String> categoryCodes) {
        
        return ruleService.removeCategoriesFromRule(id, categoryCodes)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/{id}/clone")
    @Operation(summary = "Clone rule", description = "Create a copy of an existing rule")
    public ResponseEntity<BusinessRuleDto> cloneRule(
            @PathVariable UUID id,
            @RequestParam @NotBlank String newRuleKey) {
        
        BusinessRuleDto cloned = ruleService.cloneRule(id, newRuleKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(cloned);
    }
    
    @PostMapping("/search")
    @Operation(summary = "Search rules", description = "Search rules with complex criteria")
    public ResponseEntity<List<BusinessRuleDto>> searchRules(
            @RequestBody RuleSearchRequest searchRequest) {
        
        List<BusinessRuleDto> results = ruleService.searchRules(searchRequest);
        return ResponseEntity.ok(results);
    }
    
    @PostMapping("/validate")
    @Operation(summary = "Validate rule content", description = "Validate DRL content before saving")
    public ResponseEntity<ValidationResult> validateRule(
            @RequestBody @NotBlank String ruleContent) {
        
        ValidationResult result = ruleService.validateRuleContent(ruleContent);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/execute")
    @Operation(summary = "Execute rules", description = "Execute rules for specified categories")
    public ResponseEntity<RuleExecutionResultDto> executeRules(
            @Valid @RequestBody RuleExecutionRequest request) {
        
//        log.info("Executing rules for categories: {}", request.getCategories());
        RuleExecutionResultDto result = executionService.executeRules(request);
        return ResponseEntity.ok(result);
    }
}