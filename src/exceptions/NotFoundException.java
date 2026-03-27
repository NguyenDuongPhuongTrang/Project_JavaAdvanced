package exceptions;

// không tìm thấy user, không tìm thấy order
public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(message);
    }
}
