package users;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
    public static byte[] hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-224");
            return digest.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-224 algorithm not found", e);
        }
    }

    // Для сравнения хэшей
    public static boolean isPasswordMatch(String inputPassword, byte[] storedHash) {
        byte[] inputHash = hashPassword(inputPassword);
        return MessageDigest.isEqual(inputHash, storedHash);
    }
}