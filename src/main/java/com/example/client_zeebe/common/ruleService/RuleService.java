package com.example.clientZeebe.common.ruleService;

import com.example.clientZeebe.common.drools.dto.RuleTaskHeaderModel;
import com.example.clientZeebe.common.exceptions.RuleExecutionTypeNotDefinedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RuleService {


    protected Map<String,String> getAllHeaders(ActivatedJob job) {
        return job.getCustomHeaders();
    }

    protected RuleTaskHeaderModel getRuleExecutionType(ActivatedJob job) throws RuleExecutionTypeNotDefinedException {
        String ruleType = getAllHeaders(job).getOrDefault("rule_header",null);
        var jsonMapper = JsonMapper.builder()
                .build();
        if (ruleType == null)
            throw new RuleExecutionTypeNotDefinedException(String.format("Process Definition error, no such rule type [%s] is defined.", ruleType));

        RuleTaskHeaderModel ruleTaskHeaderModel = null;
        try {
            ruleTaskHeaderModel = jsonMapper.readValue(ruleType, RuleTaskHeaderModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        return ruleTaskHeaderModel;
    }

}
