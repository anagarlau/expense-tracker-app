package de.htwberlin.webtech.expensetracker.exceptions;



public class ResourceNotFound extends RuntimeException  {
    public ResourceNotFound(String errorMessage){

        super(errorMessage);
    }
}
