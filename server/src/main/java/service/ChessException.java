package service;

public class ChessException extends Exception {
    private final int errorCode;

    public ChessException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getCode() {
        return errorCode;
    }
}
