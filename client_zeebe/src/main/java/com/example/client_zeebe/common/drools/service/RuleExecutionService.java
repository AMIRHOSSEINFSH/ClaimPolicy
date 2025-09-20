package com.example.client_zeebe.common.drools.service;
// ==================== Rule Execution Service ====================

import com.example.client_zeebe.common.drools.dto.RuleExecutionRequest;
import com.example.client_zeebe.common.drools.dto.RuleExecutionResultDto;
import com.example.client_zeebe.common.drools.dto.ValidationError;
import com.example.client_zeebe.common.drools.entity.BusinessRule;
import com.example.client_zeebe.common.drools.entity.RuleExecutionLog;
import com.example.client_zeebe.common.drools.enums.ExecutionStatus;
import com.example.client_zeebe.common.drools.exception.RuleCompilationException;
import com.example.client_zeebe.common.drools.repository.BusinessRuleRepository;
import com.example.client_zeebe.common.drools.repository.RuleExecutionLogRepository;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class RuleExecutionService {
    
    private final BusinessRuleRepository ruleRepository;
    private final RuleExecutionLogRepository logRepository;
    private final KieServices kieServices;

    public RuleExecutionService(BusinessRuleRepository ruleRepository, RuleExecutionLogRepository logRepository) {
        this.ruleRepository = ruleRepository;
        this.logRepository = logRepository;
        this.kieServices = KieServices.Factory.get();
    }

    private final Map<String, KieBase> compiledRules = new ConcurrentHashMap<>();
    
    public RuleExecutionResultDto executeRules(RuleExecutionRequest request) {
        long startTime = System.currentTimeMillis();
        String executionId = UUID.randomUUID().toString();
        
        try {
            // Load rules for categories
            List<BusinessRule> rules = List.of();//ruleRepository.findActiveRulesByCategoriesIn(request.getCategories());
            
            if (rules.isEmpty()) {
//                log.warn("No active rules found for categories: {}", request.getCategories());
                return createEmptyResult(executionId, request);
            }
            
            // Compile rules
            KieBase kieBase = compileRules(rules, request.getCategories());
            
            // Create session and execute
            KieSession session = kieBase.newKieSession();
            
            try {
                // Insert facts
                request.getFacts().forEach((key, value) -> {
                    session.insert(value);
                    session.setGlobal(key, value);
                });
                
                // Fire rules
                int rulesFired = session.fireAllRules();
                
                // Extract results
                Map<String, Object> outputData = extractOutputData(session);
                
                // Create result
//                RuleExecutionResultDto result = RuleExecutionResultDto.builder()
//                        .executionId(executionId)
//                        .processInstanceId(request.getProcessInstanceId())
//                        .categoriesExecuted(request.getCategories())
//                        .rulesFired(rulesFired)
//                        .inputData(request.getFacts())
//                        .outputData(outputData)
//                        .executedRules(getExecutedRules(session))
//                        .executionTimeMs(System.currentTimeMillis() - startTime)
//                        .status(ExecutionStatus.SUCCESS)
//                        .executedAt(LocalDateTime.now())
//                        .build();
                RuleExecutionResultDto result = new RuleExecutionResultDto();

                result.setExecutionId(executionId);
                result.setProcessInstanceId(request.getProcessInstanceId());
                result.setCategoriesExecuted(request.getCategories());
                result.setRulesFired(rulesFired);
                result.setInputData(request.getFacts());
                result.setOutputData(outputData);
                result.setExecutedRules(getExecutedRules(session));
                result.setExecutionTimeMs(System.currentTimeMillis() - startTime);
                result.setStatus(ExecutionStatus.SUCCESS);
                result.setExecutedAt(LocalDateTime.now());


                // Log execution
                logExecution(result, rules);
                
                return result;
                
            } finally {
                session.dispose();
            }
            
        } catch (Exception e) {
//            log.error("Error executing rules for categories: {}", request.getCategories(), e);
            
            RuleExecutionResultDto errorResult = createErrorResult(executionId, request, e);
            logExecution(errorResult, null);
            
            return errorResult;
        }
    }
    
    private KieBase compileRules(List<BusinessRule> rules, Set<String> categories) {
        String cacheKey = categories.stream()
                .sorted()
                .collect(Collectors.joining("-"));
        
        return compiledRules.computeIfAbsent(cacheKey, key -> {
            KieFileSystem kfs = kieServices.newKieFileSystem();
            
            // Add rules to file system
            for (BusinessRule rule : rules) {
                String path = String.format("src/main/resources/rules/%s.drl", rule.getRuleName());
                kfs.write(path, rule.getRuleContent());
            }
            
            // Build
            KieBuilder kieBuilder = kieServices.newKieBuilder(kfs);
            Results results = kieBuilder.buildAll().getResults();
            
            if (results.hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
                throw new RuleCompilationException("Failed to compile rules: " +
                        results.getMessages());
            }
            
            return kieServices.newKieContainer(kieBuilder.getKieModule().getReleaseId()).getKieBase();
        });
    }
    
    private Map<String, Object> extractOutputData(KieSession session) {
        Map<String, Object> output = new HashMap<>();
        
        // Extract all facts from session
        session.getObjects().forEach(obj -> {
            String className = obj.getClass().getSimpleName();
            output.put(className, obj);
        });
        
        return output;
    }
    
    private List<String> getExecutedRules(KieSession session) {
        // This would require custom AgendaEventListener to track fired rules
        return new ArrayList<>();
    }
    
    private void logExecution(RuleExecutionResultDto result, List<BusinessRule> rules) {
//        RuleExecutionLog log = RuleExecutionLog.builder()
//                .processInstanceId(result.getProcessInstanceId())
//                .categoriesExecuted(result.getCategoriesExecuted())
//                .inputData(result.getInputData())
//                .outputData(result.getOutputData())
//                .rulesFired(result.getRulesFired())
//                .executionTimeMs(result.getExecutionTimeMs())
//                .status(result.getStatus())
//                .errorMessage(result.getErrors() != null && !result.getErrors().isEmpty()
//                        ? result.getErrors().get(0).getMessage() : null)
//                .build();
        RuleExecutionLog log = new RuleExecutionLog();

        log.setProcessInstanceId(result.getProcessInstanceId());
        log.setCategoriesExecuted(result.getCategoriesExecuted());
        log.setInputData(result.getInputData());
        log.setOutputData(result.getOutputData());
        log.setRulesFired(result.getRulesFired());
        log.setExecutionTimeMs(result.getExecutionTimeMs());
        log.setStatus(result.getStatus());
        log.setErrorMessage(
                (result.getErrors() != null && !result.getErrors().isEmpty())
                        ? result.getErrors().getFirst().getMessage()
                        : null
        );


        logRepository.save(log);
    }
    
    private RuleExecutionResultDto createEmptyResult(String executionId, RuleExecutionRequest request) {
        RuleExecutionResultDto result = new RuleExecutionResultDto();

        result.setExecutionId(executionId);
        result.setProcessInstanceId(request.getProcessInstanceId());
        result.setCategoriesExecuted(request.getCategories());
        result.setRulesFired(0);
        result.setInputData(request.getFacts());
        result.setOutputData(new HashMap<>());
        result.setExecutedRules(new ArrayList<>());
        result.setExecutionTimeMs(0L);
        result.setStatus(ExecutionStatus.SUCCESS);
        result.setExecutedAt(LocalDateTime.now());
return result;
//        return RuleExecutionResultDto.builder()
//                .executionId(executionId)
//                .processInstanceId(request.getProcessInstanceId())
//                .categoriesExecuted(request.getCategories())
//                .rulesFired(0)
//                .inputData(request.getFacts())
//                .outputData(new HashMap<>())
//                .executedRules(new ArrayList<>())
//                .executionTimeMs(0L)
//                .status(ExecutionStatus.SUCCESS)
//                .executedAt(LocalDateTime.now())
//                .build();
    }
    
    private RuleExecutionResultDto createErrorResult(String executionId, RuleExecutionRequest request, Exception e) {
//        ValidationError error = ValidationError.builder()
//                .code("EXECUTION_ERROR")
//                .message(e.getMessage())
//                .build();
//
//        return RuleExecutionResultDto.builder()
//                .executionId(executionId)
//                .processInstanceId(request.getProcessInstanceId())
//                .categoriesExecuted(request.getCategories())
//                .rulesFired(0)
//                .inputData(request.getFacts())
//                .errors(Arrays.asList(error))
//                .executionTimeMs(0L)
//                .status(ExecutionStatus.FAILED)
//                .executedAt(LocalDateTime.now())
//                .build();
        ValidationError error = new ValidationError();
        error.setCode("EXECUTION_ERROR");
        error.setMessage(e.getMessage());

        RuleExecutionResultDto result = new RuleExecutionResultDto();
        result.setExecutionId(executionId);
        result.setProcessInstanceId(request.getProcessInstanceId());
        result.setCategoriesExecuted(request.getCategories());
        result.setRulesFired(0);
        result.setInputData(request.getFacts());
        result.setErrors(Arrays.asList(error));
        result.setExecutionTimeMs(0L);
        result.setStatus(ExecutionStatus.FAILED);
        result.setExecutedAt(LocalDateTime.now());

        return result;

    }
}