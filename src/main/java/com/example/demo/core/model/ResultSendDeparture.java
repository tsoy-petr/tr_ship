package com.example.demo.core.model;

public class ResultSendDeparture {

    private Boolean isSuccess;
    private String message;

    public ResultSendDeparture(Boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }
}
