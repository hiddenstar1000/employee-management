package net.dixonai.employeemanagement.exception;

import com.mongodb.MongoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MongoException.class)
    public ResponseEntity<Map<String, Object>> handleMongoException(MongoException ex) {
        logger.error("Database connection exception caught in GlobalExceptionHandler: {}", ex.getMessage(), ex);
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        body.put("error", "Database Connection Error");
        body.put("message", "Unable to connect to MongoDB cluster. Please check your network connection and MongoDB Atlas IP whitelist (0.0.0.0/0).");
        body.put("details", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }
}

