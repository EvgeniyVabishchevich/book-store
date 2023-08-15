package com.andersen.controllers.servlet.jsonBodies;

import java.util.List;

public class JsonOrder {
    private Long clientId;
    private List<JsonRequest> requests;

    public JsonOrder() {
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public List<JsonRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<JsonRequest> requests) {
        this.requests = requests;
    }
}
