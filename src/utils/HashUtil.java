package utils;

import org.mindrot.jbcrypt.BCrypt;

public class HashUtil {
    public static String hashPassword(String password) {
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt(10));
        return hashedPassword;
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return org.mindrot.jbcrypt.BCrypt.checkpw(password, hashedPassword);
    }
}