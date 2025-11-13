package exception;

public class ResponseException extends Exception {
    public enum Code {
        BadRequest, Unauthorized, Forbidden, NotFound, ServerError, Unknown
    }

    private final Code code;

    public ResponseException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    // Add this helper so we can easily map HTTP status codes
    public static Code fromHttpStatusCode(int statusCode) {
        return switch (statusCode) {
            case 400 -> Code.BadRequest;
            case 401 -> Code.Unauthorized;
            case 403 -> Code.Forbidden;
            case 404 -> Code.NotFound;
            case 500 -> Code.ServerError;
            default -> Code.Unknown;
        };
    }
}
