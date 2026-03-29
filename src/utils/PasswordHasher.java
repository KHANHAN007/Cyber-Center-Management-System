package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {
    private static final int BCRYPT_STRENGTH = 10;

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_STRENGTH));
    }

    public static boolean verifyPassword(String password, String storedHash) {
        try {
            return BCrypt.checkpw(password, storedHash);
        } catch (Exception e) {
            throw new RuntimeException("Error verifying password: " + e.getMessage());
        }
    }
}
