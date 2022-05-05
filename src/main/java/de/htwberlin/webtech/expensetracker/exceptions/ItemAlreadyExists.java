package de.htwberlin.webtech.expensetracker.exceptions;


/*TODO: GLOBAL ERROR HANDLING*/
public class ItemAlreadyExists extends RuntimeException{
    public ItemAlreadyExists(String errorMessage){

        super(errorMessage);
    }
}
