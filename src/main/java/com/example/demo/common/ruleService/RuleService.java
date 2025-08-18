package com.example.demo.common.ruleService;

import com.example.demo.common.enums.Const;
import com.example.demo.common.exceptions.RuleExecutionTypeNotDefinedException;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RuleService {

    protected Map<String,String> getAllHeaders(ActivatedJob job) {
        return job.getCustomHeaders();
    }

    protected Const.RuleExecutionType getRuleExecutionType(ActivatedJob job) throws RuleExecutionTypeNotDefinedException {
        String ruleType = getAllHeaders(job).getOrDefault("rule_exectuation_type",null);
        Const.RuleExecutionType ruleExecutionType = Const.RuleExecutionType.fromStringOrNull(ruleType);
        if (ruleType == null || ruleExecutionType == null) {
            throw new RuleExecutionTypeNotDefinedException(String.format("Process Definition error, no such rule type [%s] is defined.", ruleType));
        }
        return ruleExecutionType;
    }

}
