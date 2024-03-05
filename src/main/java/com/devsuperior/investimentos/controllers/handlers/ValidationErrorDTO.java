package com.devsuperior.investimentos.controllers.handlers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationErrorDTO extends CustomErrorDTO{

    private List<FiledMessage> errors = new ArrayList<>();

    public ValidationErrorDTO(Instant timestamp, Integer status, String error, String path) {
        super(timestamp, status, error, path);
    }

    public List<FiledMessage> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String message){
        errors.add(new FiledMessage(fieldName, message));
    }
}
