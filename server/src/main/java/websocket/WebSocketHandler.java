package websocket;

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
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.messages.*;
import websocket.commands.*;
import static websocket.commands.UserGameCommand.CommandType.*;
import static websocket.messages.ServerMessage.ServerMessageType.*;

import java.io.IOException;

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
            String username = dataAccess.getUsername(command.getAuthToken());
            connections.add(gameID, session);
            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> System.out.print("move");
                case LEAVE -> System.out.print("leave");
                case RESIGN -> System.out.print("resign");
            }
        } catch (DataAccessException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(Session session, String username, UserGameCommand command) throws IOException {
        int gameID = command.getGameID();
        connections.add(gameID, session);
        var message = String.format("%s has joined the game", username);
        var serverMessage = new ServerMessage(NOTIFICATION, message);
        connections.broadcast(gameID, session, serverMessage);
    }

    private void leave(Session session, String username, UserGameCommand command) throws IOException {
        int gameID = command.getGameID();
        var message = String.format("%s left the game", username);
        var serverMessage = new ServerMessage(NOTIFICATION, message);
        connections.broadcast(gameID, session, serverMessage);
        connections.remove(gameID, session);
    }
}
