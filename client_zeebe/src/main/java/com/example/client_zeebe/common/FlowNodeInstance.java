package com.example.client_zeebe.common;

import java.time.OffsetDateTime;

public class FlowNodeInstance {

    private Long key;

    private Long processInstanceKey;

    private Long processDefinitionKey;

    private OffsetDateTime startDate;

    private String flowNodeId;

    private String flowNodeName;

    private Long incidentKey;

    private String type;

    private String state;

    private Boolean incident;

    private String tenantId;

    // Constructors
    public FlowNodeInstance() {
    }

    public FlowNodeInstance(Long key, Long processInstanceKey, Long processDefinitionKey,
                            OffsetDateTime startDate, String flowNodeId, String flowNodeName,
                            Long incidentKey, String type, String state, Boolean incident,
                            String tenantId) {
        this.key = key;
        this.processInstanceKey = processInstanceKey;
        this.processDefinitionKey = processDefinitionKey;
        this.startDate = startDate;
        this.flowNodeId = flowNodeId;
        this.flowNodeName = flowNodeName;
        this.incidentKey = incidentKey;
        this.type = type;
        this.state = state;
        this.incident = incident;
        this.tenantId = tenantId;
    }

    // Getters and Setters
    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Long getProcessInstanceKey() {
        return processInstanceKey;
    }

    public void setProcessInstanceKey(Long processInstanceKey) {
        this.processInstanceKey = processInstanceKey;
    }

    public Long getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(Long processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public String getFlowNodeId() {
        return flowNodeId;
    }

    public void setFlowNodeId(String flowNodeId) {
        this.flowNodeId = flowNodeId;
    }

    public String getFlowNodeName() {
        return flowNodeName;
    }

    public void setFlowNodeName(String flowNodeName) {
        this.flowNodeName = flowNodeName;
    }

    public Long getIncidentKey() {
        return incidentKey;
    }

    public void setIncidentKey(Long incidentKey) {
        this.incidentKey = incidentKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getIncident() {
        return incident;
    }

    public void setIncident(Boolean incident) {
        this.incident = incident;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
