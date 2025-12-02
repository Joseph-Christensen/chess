package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import exception.ResponseException;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.messages.*;
import websocket.commands.*;
import static websocket.commands.UserGameCommand.CommandType.*;
import static websocket.messages.ServerMessage.ServerMessageType.*;

import java.io.IOException;
import java.util.Objects;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final Gson serializer = new Gson();
    private final DataAccess dataAccess;

    public WebSocketHandler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        int gameID;
        Session session = ctx.session;

        try {
            UserGameCommand command = serializer.fromJson(ctx.message(), UserGameCommand.class);
            gameID = command.getGameID();
            connections.add(gameID, session);
            switch (command.getCommandType()) {
                case CONNECT -> connect(session, command);
                case MAKE_MOVE -> System.out.print("move");
                case LEAVE -> System.out.print("leave");
                case RESIGN -> System.out.print("resign");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(Session session, UserGameCommand command) throws IOException {
        try {
            int gameID = command.getGameID();
            String authToken = command.getAuthToken();
            AuthData auth = dataAccess.getAuth(authToken);
            if (auth == null) {
                connections.sendSelf(session, new ErrorMessage("Error: Unauthorized"));
                return;
            }
            String username = auth.username();
            GameData game = dataAccess.getGame(gameID);

            if (game == null) {
                connections.sendSelf(session, new ErrorMessage("Error: game not found."));
                return;
            }

            connections.add(gameID, session);
            String color;
            if (username.equals(game.whiteUsername())) {
                color = "white";
            } else if (username.equals(game.blackUsername())) {
                color = "black";
            } else {
                color = "an observer";
            }
            connections.sendSelf(session, new LoadGameMessage(game.toString()));

            var message = String.format("%s has joined the game as %s.", username, color);
            var serverMessage = new ServerMessage(NOTIFICATION, message);
            connections.broadcast(gameID, session, serverMessage);
        } catch (DataAccessException ex) {
            connections.sendSelf(session, new ErrorMessage(ex.getMessage()));
        }
    }

    private void leave(Session session, String username, UserGameCommand command) throws IOException {
        int gameID = command.getGameID();
        var message = String.format("%s left the game", username);
        var serverMessage = new ServerMessage(NOTIFICATION, message);
        connections.broadcast(gameID, session, serverMessage);
        connections.remove(gameID, session);
    }
}
