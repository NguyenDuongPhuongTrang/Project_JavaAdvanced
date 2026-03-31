package validation;

public class ValidatePhone {
    public static boolean isValidatePhone(String phone) {
        return phone.matches("^0[3-9][0-9]{8}$");
    }
}
