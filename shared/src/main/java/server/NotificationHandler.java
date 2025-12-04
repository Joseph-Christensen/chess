package server;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

public interface NotificationHandler {
    void notify(ServerMessage notification);
    void displayGame(LoadGameMessage loadGameMessage);
    void error(ErrorMessage errorMessage);
}
