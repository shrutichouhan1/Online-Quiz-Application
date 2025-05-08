import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // Method to hash a password
    public static String hashPassword(String password) {
        // The gensalt() function generates a random salt and hashes the password
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Method to verify if the password matches the stored hash
    public static boolean checkPassword(String password, String storedHash) {
        // This method checks if the entered password matches the stored hash
        return BCrypt.checkpw(password, storedHash);
    }
}
