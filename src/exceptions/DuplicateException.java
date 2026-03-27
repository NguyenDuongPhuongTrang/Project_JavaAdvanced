package exceptions;

// Trùng dữ liệu: username đã tồn tại, email trùng
public class DuplicateException extends AppException {
    public DuplicateException(String message) {
        super(message);
    }
}
