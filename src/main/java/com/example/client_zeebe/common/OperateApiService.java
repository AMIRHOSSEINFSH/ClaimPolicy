package com.example.clientZeebe.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OperateApiService {

    @Autowired
    private WebClient webClient;


    public Mono<String> getProcessInstance(String processInstanceKey) {
        return webClient
                .get()
                .uri("/v1/process-instances/{key}", processInstanceKey)
                .header("Accept", "application/json")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getIncidents(String processInstanceKey) {
        return webClient
                .get()
                .uri("/v1/incidents?processInstanceKey={key}", processInstanceKey)
                .header("Accept", "application/json")
                .retrieve()
                .bodyToMono(String.class);
    }
    public String fetchIncidentTaskNames(long processInstanceKey) {
         String f = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/incidents/search")

                        .build()).bodyValue(bodyValue(String.valueOf(processInstanceKey)))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println(f);
        return f;
    }

    private String bodyValue(String instanceKey ) {
        return String.format(
                "{\n" +
                        "  \"filter\": {\n" +
                        "    \"processInstanceKey\": %s,\n" +
                "\"size\": 10 "+
                        "  }\n" +
                        "}"
        , instanceKey);
    }

    public FlowNodeResponse getTask(String processInstanceKey) {
        return webClient.post().uri(uriBuilder -> uriBuilder
                        .path("/v1/flownode-instances/search")
                        .build())
                .bodyValue(bodyValue(processInstanceKey))
                .retrieve().bodyToMono(FlowNodeResponse.class).block();
    }

}
