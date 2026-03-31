package validation;

public class ValidatePassword {
    public static boolean isValidatePassword(String password) {
        return password.matches("^.{5,}$");
    }
}
