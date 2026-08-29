package net.dixonai.employeemanagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class PasswordEncryptionService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int GCM_IV_LENGTH = 12;
    private static final String ENCRYPTED_PREFIX = "ENC_GCM_v1:";

    private final SecretKey secretKey;
    private final PasswordEncoder passwordEncoder;

    public PasswordEncryptionService(@Value("${app.security.encryption-key:default_secret_key_string_for_testing}") String rawKey) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = digest.digest(rawKey.getBytes(StandardCharsets.UTF_8));
            this.secretKey = new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize AES-256 encryption key", e);
        }
    }

    /**
     * Hashes a raw plaintext password using BCrypt.
     */
    public String encrypt(String plaintextPassword) {
        if (plaintextPassword == null || plaintextPassword.trim().isEmpty()) {
            return null;
        }

        if (isBCrypt(plaintextPassword)) {
            return plaintextPassword;
        }

        return passwordEncoder.encode(plaintextPassword);
    }

    /**
     * Checks if the stored password string is BCrypt hashed.
     */
    public boolean isBCrypt(String password) {
        return password != null && (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$"));
    }

    /**
     * Checks if the stored password string is legacy AES-256-GCM encrypted.
     */
    public boolean isEncrypted(String password) {
        return password != null && password.startsWith(ENCRYPTED_PREFIX);
    }

    /**
     * Legacy decryption method for AES-256-GCM encrypted passwords.
     */
    public String decrypt(String encryptedPassword) {
        if (encryptedPassword == null || !isEncrypted(encryptedPassword)) {
            return encryptedPassword;
        }

        try {
            String base64Data = encryptedPassword.substring(ENCRYPTED_PREFIX.length());
            byte[] combined = Base64.getDecoder().decode(base64Data);

            if (combined.length <= GCM_IV_LENGTH) {
                return null;
            }

            byte[] iv = new byte[GCM_IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, GCM_IV_LENGTH);

            byte[] ciphertext = new byte[combined.length - GCM_IV_LENGTH];
            System.arraycopy(combined, GCM_IV_LENGTH, ciphertext, 0, ciphertext.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] plaintextBytes = cipher.doFinal(ciphertext);
            return new String(plaintextBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Checks raw password against stored password supporting BCrypt, legacy AES-256-GCM, and legacy plaintext.
     */
    public boolean matches(String rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null) {
            return false;
        }

        // 1. Check if stored password is BCrypt
        if (isBCrypt(storedPassword)) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }

        // 2. Check if stored password is legacy AES-256-GCM
        if (isEncrypted(storedPassword)) {
            String decrypted = decrypt(storedPassword);
            return decrypted != null && rawPassword.equals(decrypted);
        }

        // 3. Fallback to legacy plaintext check
        return rawPassword.equals(storedPassword);
    }
}


