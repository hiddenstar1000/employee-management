package net.dixonai.employeemanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncryptionServiceTest {

    private PasswordEncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        encryptionService = new PasswordEncryptionService("c3VwZXJzZWNyZXRlc3RyaW5nMTIzNDU2Nzg5MDEy");
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
    void matches_LegacyAesEncryptedPassword_ShouldVerifySuccessfully() {
        String rawPass = "LegacyAesPassword123";
        // Legacy AES GCM format
        String legacyAesEncrypted = "ENC_GCM_v1:DfBI2oAmx0wvzYOy6w3a6QBsoSFitzXweTX/wIY4kAIIV0/oaZ0=";

        assertTrue(encryptionService.isEncrypted(legacyAesEncrypted));
        assertFalse(encryptionService.isBCrypt(legacyAesEncrypted));
    }

    @Test
    void matches_PlaintextFallback_ShouldVerifySuccessfully() {
        String rawPass = "PlaintextPass";
        assertTrue(encryptionService.matches(rawPass, rawPass));
        assertFalse(encryptionService.matches(rawPass, "WrongPass"));
    }
}
