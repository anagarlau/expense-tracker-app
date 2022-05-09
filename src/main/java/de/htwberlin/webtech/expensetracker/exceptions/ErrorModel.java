package de.htwberlin.webtech.expensetracker.exceptions;

import lombok.Data;



@Data
public class ErrorModel {
    private Integer statusCode;
    private String errorMessage;
    private String errorType;
}
