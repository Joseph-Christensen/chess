package websocket;

import chess.*;
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

import static chess.ChessGame.TeamColor.*;
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
                case MAKE_MOVE -> move(session, serializer.fromJson(ctx.message(), MakeMoveCommand.class));
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

    private void move(Session session, MakeMoveCommand command) throws IOException {
        try {
            int gameID = command.getGameID();
            String authToken = command.getAuthToken();
            ChessMove move = command.getMove();

            // check assumptions
            AuthData auth = dataAccess.getAuth(authToken);
            if (auth == null) {
                connections.sendSelf(session, new ErrorMessage("Error: Unauthorized"));
                return;
            }
            String username = auth.username();

            GameData gameData = dataAccess.getGame(gameID);
            if (gameData == null) {
                connections.sendSelf(session, new ErrorMessage("Error: Game not found."));
                return;
            }

            if (!username.equals(gameData.whiteUsername()) && !username.equals(gameData.blackUsername())) {
                connections.sendSelf(session, new ErrorMessage("Error: Observers can't play."));
                return;
            }

            ChessGame game = serializer.fromJson(String.valueOf(gameData.game()), ChessGame.class);

            ChessGame.TeamColor turn = game.getTeamTurn();
            if ((turn == WHITE && !username.equals(gameData.whiteUsername())) ||
                    (turn == BLACK && !username.equals(gameData.blackUsername()))) {
                connections.sendSelf(session, new ErrorMessage("Error: Not your turn."));
                return;
            }

            // make the move
            try {
                game.makeMove(move);
            } catch (InvalidMoveException ex) {
                connections.sendSelf(session, new ErrorMessage("Error: " + ex.getMessage()));
                return;
            }

            GameData updatedGameData = new GameData(
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    gameData.blackUsername(),
                    gameData.gameName(),
                    game
            );

            dataAccess.updateGame(updatedGameData);

            // broadcast the new game to all
            connections.broadcast(gameID, null, new LoadGameMessage(game.toString()));

            // notify what move was made
            String start = getPositionString(move.getStartPosition());
            String end = getPositionString(move.getEndPosition());

            String moveMsg = String.format("%s moved from %s to %s.", username, start, end);
            connections.broadcast(gameID, session, new ServerMessage(NOTIFICATION, moveMsg));

            // check for checkmate, check, stalemate
            ChessGame.TeamColor opponent = (turn == WHITE) ? BLACK : WHITE;
            if (game.isInCheckmate(opponent)) {
                connections.broadcast(gameID, null,
                        new ServerMessage(NOTIFICATION, opponent + " is in checkmate!"));
            } else if (game.isInCheck(opponent)) {
                connections.broadcast(gameID, null,
                        new ServerMessage(NOTIFICATION, opponent + " is in check!"));
            } else if (game.isInStalemate(opponent)) {
                connections.broadcast(gameID, null,
                        new ServerMessage(NOTIFICATION, opponent + " is in stalemate!"));
            }
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

    private String getPositionString(ChessPosition pos) {
        int row = pos.getRow();
        int col = pos.getColumn();

        switch (col) {
            case 1 -> {return "a" + row;}
            case 2 -> {return "b" + row;}
            case 3 -> {return "c" + row;}
            case 4 -> {return "d" + row;}
            case 5 -> {return "e" + row;}
            case 6 -> {return "f" + row;}
            case 7 -> {return "g" + row;}
            case 8 -> {return "h" + row;}
        }
        return "Invalid";
    }
}
