package org.advait.assignment.exception;

import org.advait.assignment.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Handle IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseUtil> handleIllegalArgumentException(IllegalArgumentException ex) {
        ResponseUtil apiError = new ResponseUtil(false,ex.getMessage(),null);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
