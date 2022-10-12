package com.example.demo.core.model;

public class ResultSendDeparture {

    private Boolean isSuccess;
    private String message;
    private String pathFile;

    public ResultSendDeparture(Boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public ResultSendDeparture(Boolean isSuccess, String message, String pathFile) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.pathFile = pathFile;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public String getPathFile() {
        return pathFile;
    }
}
