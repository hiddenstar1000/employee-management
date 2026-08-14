package net.dixonai.employeemanagement.exception;

import com.mongodb.MongoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MongoException.class)
    public ResponseEntity<Map<String, Object>> handleMongoException(MongoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        body.put("error", "Database Connection Error");
        body.put("message", "Unable to connect to MongoDB cluster. Please check your network connection and MongoDB Atlas IP whitelist (0.0.0.0/0).");
        body.put("details", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }
}
