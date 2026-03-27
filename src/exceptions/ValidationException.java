package exceptions;

// Sai định dạng: nhập sai format, password quá ngắn,...
public class ValidationException extends AppException {
    public ValidationException(String message) {
        super(message);
    }
}
