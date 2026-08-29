package net.dixonai.employeemanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncryptionServiceTest {

    private PasswordEncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        encryptionService = new PasswordEncryptionService();
    }

    @Test
    void encrypt_ShouldProduceBCryptHash_AndMatchPassword() {
        String rawPass = "MySecurePassword#2026";
        String hashed = encryptionService.encrypt(rawPass);

        assertNotNull(hashed);
        assertTrue(encryptionService.isBCrypt(hashed));
        assertTrue(encryptionService.matches(rawPass, hashed));
        assertFalse(encryptionService.matches("WrongPassword", hashed));
    }

    @Test
    void matches_PlaintextFallback_ShouldVerifySuccessfully() {
        String rawPass = "PlaintextPass";
        assertTrue(encryptionService.matches(rawPass, rawPass));
        assertFalse(encryptionService.matches(rawPass, "WrongPass"));
    }
}
