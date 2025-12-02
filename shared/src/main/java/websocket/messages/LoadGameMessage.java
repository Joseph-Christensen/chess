package websocket.messages;

public class LoadGameMessage extends ServerMessage {

    private final String game;

    public LoadGameMessage(String gameJson) {
        super(ServerMessageType.LOAD_GAME, null);
        this.game = gameJson;
    }

    public String getGame() {
        return game;
    }
}