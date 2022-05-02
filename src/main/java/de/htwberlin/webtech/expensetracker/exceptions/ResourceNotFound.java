package de.htwberlin.webtech.expensetracker.exceptions;

import java.util.function.Supplier;

public class ResourceNotFound extends RuntimeException  {
    public ResourceNotFound(String errorMessage){

        super(errorMessage);
    }
}
