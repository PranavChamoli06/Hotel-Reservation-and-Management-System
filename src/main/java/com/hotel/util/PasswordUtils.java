package com.hotel.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class PasswordUtils {

    // Generate a random 32-byte salt (hex encoded -> 64 chars)
    public static String generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[32]; // 32 bytes = 64 hex chars
        sr.nextBytes(salt);
        return bytesToHex(salt);
    }

    // Hash password with salt using SHA-256
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes(StandardCharsets.UTF_8)); // salt first
            byte[] hashed = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashed);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Verify password by hashing input and comparing to stored hash
    public static boolean verifyPassword(String password, String salt, String storedHash) {
        String calculatedHash = hashPassword(password, salt);
        return calculatedHash.equalsIgnoreCase(storedHash); // ignore case for hex
    }

    // Helper: convert byte array to hex string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
