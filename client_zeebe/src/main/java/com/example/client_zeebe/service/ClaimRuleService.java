package com.example.client_zeebe.service;

import com.example.client_zeebe.common.drools.dto.RuleTaskHeaderModel;
import com.example.client_zeebe.common.drools.entity.BusinessRule;
import com.example.client_zeebe.common.drools.repository.BusinessRuleRepository;
import com.example.client_zeebe.common.ruleService.DroolsService;
import com.example.client_zeebe.common.ruleService.RuleService;
import com.example.client_zeebe.dto.ClaimDto;
import io.camunda.zeebe.client.api.JsonMapper;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.client_zeebe.common.enums.Const.Process_claim_policy_suffix;
@Service
public class ClaimRuleService extends RuleService {

    @Autowired
    private BusinessRuleRepository businessRuleRepository;

    @Autowired
    private DroolsService droolsService;

    @Autowired
    private JsonMapper jsonMapper;

    @JobWorker(type = "dto_rules_claim_month", streamEnabled = true)
    public void checkClaimDtoRule(final JobClient client, final ActivatedJob job) {
        RuleTaskHeaderModel ruleExecutionType = getRuleExecutionType(job);
        switch (ruleExecutionType.getExecutionType()) {
            case CATEGORY_COMMON -> {
            }
            case CATEGORY_ALL -> {
            }
            case EACH_RULE -> {
                List<BusinessRule> ruleList = businessRuleRepository.findByRuleNameIn(ruleExecutionType.getNames());
                KieContainer kieContainer = droolsService.createTempContainer(ruleList,"ClaimDto");
                StatelessKieSession kieSession = kieContainer.newStatelessKieSession("ClaimDto");
                String claimJson=  jsonMapper.toJson(job.getVariable("claimDto"));
                ClaimDto claimDto = jsonMapper.fromJson(claimJson,ClaimDto.class);
                kieSession.execute(claimDto);
            }
        }
        // ...
    }

    @JobWorker(type = "entity_rules_" + Process_claim_policy_suffix, streamEnabled = true)
    public void checkClaimEntityRule(final JobClient client, final ActivatedJob job) {
        RuleTaskHeaderModel ruleExecutionType = getRuleExecutionType(job);
        switch (ruleExecutionType.getExecutionType()) {
            case CATEGORY_COMMON -> {
            }
            case CATEGORY_ALL -> {
            }
            case EACH_RULE -> {
            }
            //...
        }

    }

}
