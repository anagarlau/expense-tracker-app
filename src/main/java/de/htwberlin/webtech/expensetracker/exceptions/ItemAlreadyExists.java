package de.htwberlin.webtech.expensetracker.exceptions;

public class ItemAlreadyExists extends RuntimeException{
    public ItemAlreadyExists(String errorMessage){

        super(errorMessage);
    }
}
