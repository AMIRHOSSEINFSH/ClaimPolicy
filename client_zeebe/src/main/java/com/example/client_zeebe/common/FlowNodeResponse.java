package com.example.client_zeebe.common;

import java.util.List;

public class FlowNodeResponse {

    private List<FlowNodeInstance> items;
    private int total;

    public List<FlowNodeInstance> getItems() {
        return items;
    }

    public void setItems(List<FlowNodeInstance> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
