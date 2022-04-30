package de.htwberlin.webtech.expensetracker.exceptions;

public class IllegalCategoryException extends RuntimeException{
    public IllegalCategoryException(String errorMessage){
        super(errorMessage);
    }
}
