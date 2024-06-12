package com.EmployeeDetails.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@RestControllerAdvice
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity< Map<String,String>> handleInValidArgument(MethodArgumentNotValidException ex){
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put("Status","Not Saved");
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put("Error Message",error.getDefaultMessage());
        });
            return new ResponseEntity<>(errorMap,HttpStatus.BAD_REQUEST);
        }

    // INTERNAL_SERVER_ERROR 500 (DB)

    @org.springframework.web.bind.annotation.ExceptionHandler(CannotCreateTransactionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String,String>> handleInternalServerError(CannotCreateTransactionException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        String errorMessage = ex.getMessage();
        errors.put("Status","Not Saved");
        if (errorMessage.contains("Could not open JPA EntityManager for transaction")) {
            errorMessage = "Cannot connect to DataBase Server";
            errors.put("Error Message", errorMessage);
        }
        return new ResponseEntity<>(errors,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}


