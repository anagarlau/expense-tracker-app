package de.htwberlin.webtech.expensetracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /*handles 404s and application exceptions*/
    @ExceptionHandler({ResourceNotFound.class, ItemAlreadyExists.class})
    public ResponseEntity<ErrorModel> handlesApplicationExceptions(RuntimeException ex, WebRequest req){
         ErrorModel error = new ErrorModel();
         error.setStatusCode(HttpStatus.NOT_FOUND.value());
         error.setErrorMessage(ex.getMessage());
         error.setErrorType(ex.getClass().getTypeName());
         return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    /*handles 400 bad requests MethodArgumentTypeMismatchException - Spring ready-made exception*/
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorModel> handlesMismatchExceptions(MethodArgumentTypeMismatchException ex, WebRequest req){
        ErrorModel error = new ErrorModel();
        error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        error.setErrorMessage(ex.getMessage());
        error.setErrorType(ex.getClass().getTypeName());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /*handles 500s internal server errors*/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorModel> handlesNonSpecificExceptions(Exception ex, WebRequest req){
        ErrorModel error = new ErrorModel();
        error.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setErrorMessage(ex.getMessage());
        error.setErrorType(ex.getClass().getTypeName());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
