package de.htwberlin.webtech.expensetracker.exceptions;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
public class ErrorModel {
    private Integer statusCode;
    private Set errorMessage = new HashSet();
    private String errorType;
}
