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
    void encrypt_And_Matches_ShouldVerifyCorrectPassword() {
        String rawPass = "MySecurePassword#2026";
        String encrypted = encryptionService.encrypt(rawPass);

        assertNotNull(encrypted);
        assertTrue(encryptionService.isEncrypted(encrypted));
        assertTrue(encryptionService.matches(rawPass, encrypted));
        assertFalse(encryptionService.matches("WrongPassword", encrypted));
    }

    @Test
    void decrypt_ShouldReturnOriginalPlaintext() {
        String rawPass = "SuperSecret123!";
        String encrypted = encryptionService.encrypt(rawPass);
        String decrypted = encryptionService.decrypt(encrypted);

        assertEquals(rawPass, decrypted);
    }
}
