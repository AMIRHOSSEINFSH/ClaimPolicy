package com.example.demo.service;

import com.example.demo.dto.ClaimDto;
import com.example.demo.entity.ClaimEntity;
import com.example.demo.repository.PolicyRepository;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClaimCheckService {

    private static final Logger log = LoggerFactory.getLogger(ClaimCheckService.class);

    @Autowired
    private ClaimService claimService;

    @Autowired
    private PolicyRepository policyRepository;

    @JobWorker(type = "validatePolicy",streamEnabled = true)
    public void checkPolicy(final JobClient client, final ActivatedJob job) {
        log.info("Checking policy");
        ClaimDto claimDto = job.getVariablesAsType(ClaimDto.class);

        ClaimEntity claim = claimService.fillEntity(claimDto);

        try {
            claim = claimService.save(claim);
        }catch (Exception e) {
            log.error(e.getMessage());
            client.newThrowErrorCommand(job.getKey()).errorCode("ERR").errorMessage("ERR").variables(Map.of("errorList", List.of("ERR_1000_1","ERR_1000_2"))).send().join();
            return;
        }
        client.newCompleteCommand(job)
                .variables(
                        Map.of("madarekIsNeeded",policyRepository.findById(claimDto.getPolicyId()).get().isDocIsNeeded(),
                                "claimId",claim.getId().toString()))
                .send()
                .join();
    }

    @JobWorker(type = "policeInquiry")
    public void checkPolicyInquiry(final JobClient client, final ActivatedJob job) {
        log.info("Checking policy inquiry");
    }

    @JobWorker(type = "DamageAssesment")
    public void checkDamageAssesment(final JobClient client, final ActivatedJob job) {
        log.info("Checking damage assessment");
    }

    @JobWorker(type = "claimPayment")
    public void checkClaimPayment(final JobClient client, final ActivatedJob job) {
        log.info("Checking claim payment");
    }

}
