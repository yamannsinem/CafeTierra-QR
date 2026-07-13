package security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    /**
     * Hashes the given raw password string using BCrypt (which automatically salts).
     *
     * @param password the plaintext password to hash
     * @return the hashed password, or null if input is null
     */
    public static String hashPassword(String password) {
        if (password == null) {
            return null;
        }
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     * Checks if the plaintext password matches the hashed password.
     *
     * @param password the plaintext password to check
     * @param hashed the hashed password to check against
     * @return true if matches, false otherwise
     */
    public static boolean checkPassword(String password, String hashed) {
        if (password == null || hashed == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(password, hashed);
        } catch (Exception e) {
            return false;
        }
    }
}
