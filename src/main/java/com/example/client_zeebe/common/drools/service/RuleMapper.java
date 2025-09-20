package com.example.clientZeebe.common.drools.service;

import com.example.clientZeebe.common.drools.dto.BusinessRuleDto;
import com.example.clientZeebe.common.drools.dto.ExecutionPhaseDto;
import com.example.clientZeebe.common.drools.dto.ExecutionPlanDto;
import com.example.clientZeebe.common.drools.dto.RuleCategoryDto;
import com.example.clientZeebe.common.drools.entity.BusinessRule;
import com.example.clientZeebe.common.drools.entity.ExecutionPhase;
import com.example.clientZeebe.common.drools.entity.ExecutionPlan;
import com.example.clientZeebe.common.drools.entity.RuleCategory;
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
    default Set<String> categoriesToCodes(Set<String> categories) {
        if (categories == null) return null;
        return categories.stream()
//            .map(RuleCategory::getCategoryCode)
            .collect(Collectors.toSet());
    }
    
    @Mapping(target = "ruleCount", expression = "java(entity.getRules() != null ? entity.getRules().size() : 0)")
    RuleCategoryDto toDto(RuleCategory entity);
    
    ExecutionPlanDto toDto(ExecutionPlan entity);
    
    ExecutionPhaseDto toDto(ExecutionPhase entity);
}