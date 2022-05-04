package de.htwberlin.webtech.expensetracker.exceptions;

public class TransactionOutOfBounds extends RuntimeException{
    public TransactionOutOfBounds(String errorMessage){

        super(errorMessage);
    }
}
