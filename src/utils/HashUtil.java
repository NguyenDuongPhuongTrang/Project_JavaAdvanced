package utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class HashUtil {
    public static String hashPassword(String password) {
        BCrypt.Hasher hasher = BCrypt.withDefaults();
        String hashPassword = hasher.hashToString(12, password.toCharArray());
        return hashPassword;
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer()
                .verify(password.toCharArray(), hashedPassword);
        return result.verified;
    }
}