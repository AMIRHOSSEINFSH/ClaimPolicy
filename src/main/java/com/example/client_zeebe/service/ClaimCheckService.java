package com.example.clientZeebe.service;

import com.example.clientZeebe.dto.ClaimDto;
import com.example.clientZeebe.entity.ClaimEntity;
import com.example.clientZeebe.entity.PolicyEntity;
import com.example.clientZeebe.entity.VehicleEntity;
import com.example.clientZeebe.repository.PolicyRepository;
import com.example.clientZeebe.repository.VehicleRepository;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.clientZeebe.common.enums.Const.PROCESS.Process_claim_policy;

@Service
public class ClaimCheckService extends BaseService {

    private static final Logger log = LoggerFactory.getLogger(ClaimCheckService.class);

    @Autowired
    private ClaimService claimService;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @JobWorker(type = "validatePolicy",streamEnabled = true)
    public void checkPolicy(final JobClient client, final ActivatedJob job) {
        log.info("Checking policy");
        ClaimDto claimDto = getProcessVariableAs(job, "claimDto", ClaimDto.class);

        ClaimEntity claim = claimService.fillEntity(claimDto);

        try {
            claim = claimService.save(claim);
        } catch (Exception e) {
            log.error(e.getMessage());
            client.newThrowErrorCommand(job.getKey()).errorCode("ERR").errorMessage("ERR").variables(Map.of("errorList", List.of("ERR_1000_1", "ERR_1000_2"))).send().join();
            return;
        }
        client.newCompleteCommand(job)
                .variables(
                        Map.of("madarekIsNeeded", policyRepository.findById(claimDto.getPolicyId()).get().isDocIsNeeded(),
                                "claimId", claim.getId().toString()))
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

    @JobWorker(type = "fetchPolicy")
    public void checkPolicyFetch(final JobClient client, final ActivatedJob job) {
        log.info("Checking policy fetch");
        ClaimDto claimDto = getProcessVariableAs(job, "claimDto", ClaimDto.class);

        Optional<PolicyEntity> policyOpt = policyRepository.findById(claimDto.getPolicyId());
        if (policyOpt.isPresent()) {
            Map<String, Object> processVariables = new HashMap<>(getProcessPrefixMap(Process_claim_policy));
            processVariables.put("claimDto", claimDto);
            PolicyEntity policy = policyOpt.get();
            claimDto.setPolicy(policy);
            client
                    .newCompleteCommand(job)
                    .variables(processVariables)
                    .send()
                    .join();
        } else {
            client.newThrowErrorCommand(job.getKey())
                    .errorCode("ERR")
                    .errorMessage("ERR")
                    .variables(Map.of("errorList", List.of("ERR_1000_3")))
                    .send()
                    .join();
        }
    }

    @JobWorker(type = "fetchVehicle")
    public void checkfetchVehicle(final JobClient client, final ActivatedJob job) {
        log.info("Checking policy fetch");
        PolicyEntity policyEntity = getProcessVariableAs(job, "policy", PolicyEntity.class);
        ClaimDto claimDto = getProcessVariableAs(job, "claimDto", ClaimDto.class);

        Optional<VehicleEntity> vehicleOpt = vehicleRepository.findById(claimDto.getVehicleId());
        if (vehicleOpt.isPresent()) {
            VehicleEntity vehicle = vehicleOpt.get();
            client.newCompleteCommand(job)
                    .variable("vehicle",vehicle)
                    .send()
                    .join();
        } else {
            client.newThrowErrorCommand(job.getKey())
                    .errorCode("ERR")
                    .errorMessage("ERR")
                    .variables(Map.of("errorList", List.of("ERR_1000_4")))
                    .send()
                    .join();
        }
    }

}
