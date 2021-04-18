package com.github.rhllor.pc.service.error;

public class ErrorModel{

    private String fieldName;
    private Object rejectedValue;
    private String messageError;

    public ErrorModel(String fieldName, Object rejectedValue, String messageError) {
        this.fieldName = fieldName;
        this.rejectedValue = rejectedValue;
        this.messageError = messageError;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public Object getRejectedValue() {
        return this.rejectedValue;
    }

    public String getMessageError() {
        return this.messageError;
    }
}