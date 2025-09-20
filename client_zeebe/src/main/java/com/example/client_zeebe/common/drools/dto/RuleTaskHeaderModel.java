package com.example.client_zeebe.common.drools.dto;

import com.example.client_zeebe.common.drools.enums.ExecutionType;

import java.util.Set;

public class RuleTaskHeaderModel {

    private Set<String> names;
    private ExecutionType executionType;

    public Set<String> getNames() {
        return names;
    }

    public void setNames(Set<String> names) {
        this.names = names;
    }

    public ExecutionType getExecutionType() {
        return executionType;
    }

    public void setExecutionType(ExecutionType executionType) {
        this.executionType = executionType;
    }
}
