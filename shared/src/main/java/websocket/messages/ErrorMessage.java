package websocket.messages;

public class ErrorMessage extends ServerMessage {
    private final String errorMessage;

    public ErrorMessage(String message) {
        super(ServerMessage.ServerMessageType.ERROR, null);
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
