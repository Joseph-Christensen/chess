package server;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;
import websocket.messages.*;
import websocket.commands.*;

import jakarta.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import jakarta.websocket.ClientEndpoint;

import static websocket.commands.UserGameCommand.CommandType.*;

@ClientEndpoint
public class WebSocketFacade {

    Session session;
    NotificationHandler notificationHandler;
    Gson gson = new Gson();

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @OnMessage
                public void onMessage(String message) {
                    ServerMessage base = gson.fromJson(message, ServerMessage.class);

                    switch (base.getServerMessageType()) {
                        case LOAD_GAME -> {
                            LoadGameMessage loadGame = gson.fromJson(message, LoadGameMessage.class);
                            notificationHandler.displayGame(loadGame);
                        }
                        case ERROR -> {
                            ErrorMessage error = gson.fromJson(message, ErrorMessage.class);
                            notificationHandler.error(error);
                        }
                        case NOTIFICATION -> notificationHandler.notify(base);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    public void join(int gameID, String authToken) throws IOException {
        UserGameCommand command = new UserGameCommand(CONNECT, authToken, gameID);
        session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void makeMove(int gameID, String authToken, ChessMove move) throws IOException {
        MakeMoveCommand command = new MakeMoveCommand(authToken, gameID, move);
        session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void leave(int gameID, String authToken) throws IOException {
        UserGameCommand command = new UserGameCommand(LEAVE, authToken, gameID);
        session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void resign(int gameID, String authToken) throws IOException {
        UserGameCommand command = new UserGameCommand(RESIGN, authToken, gameID);
        session.getBasicRemote().sendText(gson.toJson(command));
    }
}