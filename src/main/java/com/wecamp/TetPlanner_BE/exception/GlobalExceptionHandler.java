package com.wecamp.TetPlanner_BE.exception;

import com.wecamp.TetPlanner_BE.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse<?>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                .body(new BaseResponse<>(
                        false,
                        ex.getMessage(),
                        null
                ));
    }

    @ExceptionHandler(InvalidCredential.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidCredential(InvalidCredential ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new BaseResponse<>(
                        false,
                        ex.getMessage(),
                        null
                ));
    }

    @ExceptionHandler(NotFound.class)
    public ResponseEntity<BaseResponse<?>> handleNotFound(NotFound ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new BaseResponse<>(
                        false,
                        ex.getMessage(),
                        null
                ));
    }
}