package com.example.demo.service;

import com.example.demo.common.FlowNodeInstance;
import com.example.demo.common.FlowNodeResponse;
import com.example.demo.common.OperateApiService;
import com.example.demo.common.ProcessResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.worker.JobWorker;
import io.camunda.zeebe.spring.client.annotation.value.ZeebeWorkerValue;
import io.camunda.zeebe.spring.client.jobhandling.JobWorkerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public abstract class BaseService {

    @Autowired
    protected ZeebeClient zeebeClient;

    @Autowired
    private JobWorkerManager jobWorkerManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OperateApiService operateApiService;

    @Value("${camunda.client.zeebe.request-timeout}")
    private Duration requestTimeOut;

    protected ProcessResult process(String processId, Object variable, Function<Map<String,Object>, Object> callback) {
        ProcessResult processResult = new ProcessResult();
        processResult.setProcessInstanceName(processId);
        ProcessInstanceEvent result;
        try {
            result = zeebeClient.newCreateInstanceCommand()
                    .bpmnProcessId(processId)
                    .latestVersion()
                    .variables(variable)
                    .send().join();
            processResult.setProcessInstanceId(result.getProcessInstanceKey());
            Mono.create(sink-> {
                ZeebeWorkerValue jobWorkerConfig = new ZeebeWorkerValue();
                jobWorkerConfig.setType("finished");
                jobWorkerConfig.setName("finished");
                jobWorkerConfig.setAutoComplete(true);
                JobWorker jobWorker =
                        jobWorkerManager.openWorker(
                                zeebeClient,
                                jobWorkerConfig,
                                (client, job) -> {
                                    client.newCompleteCommand(job).send().join();

                                    var errList = job.getVariablesAsMap().getOrDefault("errorList",null);
                                    if (errList != null) {
                                        List<String> errorList = (List<String>) errList;
                                        if (!errorList.isEmpty()) {
                                            processResult.setResult(errorList);
                                        }
                                    }else {
                                        var r= callback.apply(job.getVariablesAsMap());
                                        processResult.setResult(r);
                                        processResult.setSucceed(true);
                                    }
                                    sink.success();
                                });

                sink.onDispose(() -> jobWorkerManager.closeWorker(jobWorker));
            }).timeout(requestTimeOut)
                    .block();

        } catch (RuntimeException e) {
            //process did not reach the end event ...
//            e.printStackTrace();
            String processInstanceKey = String.valueOf(processResult.getProcessInstanceId());

            try {
                FlowNodeResponse ins = operateApiService.getTask(processInstanceKey);
                List<FlowNodeInstance> flowNodeInstanceList = ins.getItems().stream().filter(item->item.getState().equals("ACTIVE")).toList();
                Optional<FlowNodeInstance> nodeInstance = flowNodeInstanceList.stream().filter(item->item.getIncident().equals(true)).findFirst();
                if (nodeInstance.isPresent()) {
                    processResult.setResult(false);
                    processResult.setResult(nodeInstance);
                }else {
                    processResult.setResult(true);
                    processResult.setResult(flowNodeInstanceList.isEmpty() ? null : flowNodeInstanceList.getLast());
                }

            }catch (Exception e1) {
                e1.printStackTrace();
            }
        }


        return processResult;
    }
}
