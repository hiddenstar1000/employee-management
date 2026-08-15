package net.dixonai.employeemanagement.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider("c3VwZXJzZWNyZXRlc3RyaW5nMTIzNDU2Nzg5MDEy");
    }

    @Test
    void generateToken_And_GetEmailFromToken_ShouldReturnSubject() {
        String token = tokenProvider.generateToken("john.doe@example.com", "123", "Engineering");

        assertNotNull(token);
        assertTrue(tokenProvider.validateToken(token));
        assertEquals("john.doe@example.com", tokenProvider.getEmailFromToken(token));
    }

    @Test
    void validateToken_InvalidToken_ShouldReturnFalse() {
        assertFalse(tokenProvider.validateToken("invalid.jwt.token"));
    }

    @Test
    void validateToken_NullOrEmptyToken_ShouldReturnFalse() {
        assertFalse(tokenProvider.validateToken(null));
        assertFalse(tokenProvider.validateToken(""));
    }
}
