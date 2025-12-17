//package com.bankingsystem.exception;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import java.time.LocalDateTime;
//import java.util.Map;
//@ControllerAdvice
//public class GlobalExceptionHandler {
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String,Object>> handleException(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("timestamp", LocalDateTime.now(), "status", 500, "error", "Internal Server Error", "message", ex.getMessage()));
//    }
//}

package com.bankingsystem.exception;

import com.bankingsystem.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Map<String, Object>> handleAuth(AuthException ex, HttpServletRequest req) {
        return buildError(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        return buildError(HttpStatus.BAD_REQUEST, "Bad Request", "Validation failed", req.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex, HttpServletRequest req) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), req.getRequestURI());
    }

    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String error, String message, String path) {
        Map<String, Object> body = Map.of(
                "timestamp", java.time.OffsetDateTime.now().toString(),
                "status", status.value(),
                "error", error,
                "message", message,
                "path", path
        );
        return ResponseEntity.status(status).body(body);
    }
}

