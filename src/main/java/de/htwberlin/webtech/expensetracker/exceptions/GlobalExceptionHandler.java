package de.htwberlin.webtech.expensetracker.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
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

    }


    /*handles 400s MethodArgumentTypeMismatchException - Spring ready-made exception*/
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

     /*handles JSON parse errors and marks my first stackoverflow contribution 🎂*/
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorModel responseBody = new ErrorModel();
        responseBody.setStatusCode(HttpStatus.BAD_REQUEST.value());
        responseBody.getErrorMessage().add(ex.getMessage());
        responseBody.setErrorType(ex.getClass().getTypeName());
        return ResponseEntity.badRequest().body(responseBody);
    }
}
