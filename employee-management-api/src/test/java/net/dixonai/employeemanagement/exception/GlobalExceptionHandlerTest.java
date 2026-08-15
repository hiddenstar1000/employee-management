package net.dixonai.employeemanagement.exception;

import com.mongodb.MongoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleMongoException_ShouldReturn503ServiceUnavailable() {
        MongoException mongoException = new MongoException("Connection timeout");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleMongoException(mongoException);

        assertNotNull(response);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Database Connection Error", response.getBody().get("error"));
        assertEquals("Connection timeout", response.getBody().get("details"));
    }
}
