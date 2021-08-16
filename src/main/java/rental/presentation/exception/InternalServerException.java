package rental.presentation.exception;

public class InternalServerException extends AppException {

    private final int status;

    public InternalServerException(int status, String code, String message) {
        super(code, message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
