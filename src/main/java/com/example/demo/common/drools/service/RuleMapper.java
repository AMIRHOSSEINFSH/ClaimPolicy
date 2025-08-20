package com.example.demo.common.drools.service;

import com.example.demo.common.drools.dto.BusinessRuleDto;
import com.example.demo.common.drools.dto.ExecutionPhaseDto;
import com.example.demo.common.drools.dto.ExecutionPlanDto;
import com.example.demo.common.drools.dto.RuleCategoryDto;
import com.example.demo.common.drools.entity.BusinessRule;
import com.example.demo.common.drools.entity.ExecutionPhase;
import com.example.demo.common.drools.entity.ExecutionPlan;
import com.example.demo.common.drools.entity.RuleCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;



import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RuleMapper {
    
    @Mapping(target = "categoryCodes", source = "categories", qualifiedByName = "categoriesToCodes")
    BusinessRuleDto toDto(BusinessRule entity);
    
    @Named("categoriesToCodes")
    default Set<String> categoriesToCodes(Set<RuleCategory> categories) {
        if (categories == null) return null;
        return categories.stream()
            .map(RuleCategory::getCategoryCode)
            .collect(Collectors.toSet());
    }
    
    @Mapping(target = "ruleCount", expression = "java(entity.getRules() != null ? entity.getRules().size() : 0)")
    RuleCategoryDto toDto(RuleCategory entity);
    
    ExecutionPlanDto toDto(ExecutionPlan entity);
    
    ExecutionPhaseDto toDto(ExecutionPhase entity);
}