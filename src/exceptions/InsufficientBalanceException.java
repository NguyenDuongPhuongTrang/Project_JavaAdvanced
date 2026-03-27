package exceptions;

// không đủ tiền
public class InsufficientBalanceException extends AppException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
