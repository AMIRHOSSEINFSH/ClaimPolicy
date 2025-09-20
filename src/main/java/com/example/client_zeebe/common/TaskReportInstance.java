package com.example.clientZeebe.common;

import com.example.clientZeebe.common.enums.Const;

public class TaskReportInstance {

    private String id;
    private String name;
    private String taskDefinitionId;
    private String processName;
    private Const.TASK taskState;
    private Boolean isFirst;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskDefinitionId() {
        return taskDefinitionId;
    }

    public void setTaskDefinitionId(String taskDefinitionId) {
        this.taskDefinitionId = taskDefinitionId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Const.TASK getTaskState() {
        return taskState;
    }

    public void setTaskState(Const.TASK taskState) {
        this.taskState = taskState;
    }

    public Boolean getFirst() {
        return isFirst;
    }

    public void setFirst(Boolean first) {
        isFirst = first;
    }
}
