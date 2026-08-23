package com.yambol_health.yambol_health.exceptions;




import com.yambol_health.yambol_health.payloads.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HashMap<String, String>> myMethodNotValidException(MethodArgumentNotValidException e){
        HashMap<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err ->{
            String fieldName = ((FieldError)err ).getField();
            String message = err.getDefaultMessage();
            response.put(fieldName,message);
        });
        return new ResponseEntity<HashMap<String, String>>(response,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e){
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message,false);

    return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(APIException e){
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message,false);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIResponse> myHttpMessageNotReadableException(HttpMessageNotReadableException e){
        APIResponse apiResponse = new APIResponse("Request body is invalid or has unsupported values",false);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<APIResponse> myMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        APIResponse apiResponse = new APIResponse("Invalid value for parameter: " + e.getName(),false);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

}
