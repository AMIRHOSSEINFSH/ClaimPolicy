package com.example.demo.service;

import com.example.demo.common.enums.Const;
import com.example.demo.common.ruleService.RuleService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

import java.util.Map;

import static com.example.demo.common.enums.Const.Process_claim_policy_suffix;

public class ClaimRuleService extends RuleService {

    @JobWorker(type = "dto_rules_" + Process_claim_policy_suffix ,streamEnabled = true)
    public void checkClaimDtoRule(final JobClient client, final ActivatedJob job) {
        Const.RuleExecutionType ruleExecutionType = getRuleExecutionType(job);
        // ...
    }

    @JobWorker(type = "entity_rules_" + Process_claim_policy_suffix ,streamEnabled = true)
    public void checkClaimEntityRule(final JobClient client, final ActivatedJob job) {
        Const.RuleExecutionType ruleExecutionType = getRuleExecutionType(job);
        //...
    }

}
