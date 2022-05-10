package de.htwberlin.webtech.expensetracker.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /*handles 404s and application exceptions*/
    @ExceptionHandler({ResourceNotFound.class, ItemAlreadyExists.class})
    public ResponseEntity<ErrorModel> handlesApplicationExceptions(RuntimeException ex, WebRequest req){
         ErrorModel error = new ErrorModel();
         error.setStatusCode(HttpStatus.NOT_FOUND.value());
        error.getErrorMessage().add(ex.getMessage());
         error.setErrorType(ex.getClass().getTypeName());
         return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        //return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    /*handles 400 bad requests MethodArgumentTypeMismatchException - Spring ready-made exception*/
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorModel> handlesMismatchExceptions(MethodArgumentTypeMismatchException ex, WebRequest req){
        ErrorModel error = new ErrorModel();
        error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        error.getErrorMessage().add(ex.getMessage());
        error.setErrorType(ex.getClass().getTypeName());
        return ResponseEntity.badRequest().body(error);
      }

    /*handles 500s internal server errors*/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorModel> handlesNonSpecificExceptions(Exception ex, WebRequest req){
        ErrorModel error = new ErrorModel();
        error.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.getErrorMessage().add(ex.getMessage());
        error.setErrorType(ex.getClass().getTypeName());
        return ResponseEntity.internalServerError().body(error);
     }

    /*customize validation error message*/

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorModel responseBody = new ErrorModel();
        responseBody.setStatusCode( HttpStatus.BAD_REQUEST.value());
        ex.getBindingResult().getFieldErrors().stream().forEach(e -> responseBody.getErrorMessage().add(e.getDefaultMessage()));
        responseBody.setErrorType("Validation Error");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
      }
}
