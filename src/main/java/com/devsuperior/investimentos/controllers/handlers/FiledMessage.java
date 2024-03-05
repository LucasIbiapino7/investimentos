package com.devsuperior.investimentos.controllers.handlers;

public class FiledMessage {

    private String fieldName;
    private String message;

    public FiledMessage() {
    }

    public FiledMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }
}
