package exceptions;

// Login sai: Sai mật khẩu, User không tồn tại
public class AuthenticationException extends AppException {
    public AuthenticationException(String message) {
        super(message);
    }
}
