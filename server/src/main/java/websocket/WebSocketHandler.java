package websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
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
            connections.add(gameID, session);
            switch (command.getCommandType()) {
                case CONNECT -> connect(session, command);
                case MAKE_MOVE -> move(session, serializer.fromJson(ctx.message(), MakeMoveCommand.class));
                case LEAVE -> leave(session, command);
                case RESIGN -> resign(session, command);
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
            GameData gameData = dataAccess.getGame(gameID);

            if (gameData == null) {
                connections.sendSelf(session, new ErrorMessage("Error: game not found."));
                return;
            }

            connections.add(gameID, session);
            String color;
            if (username.equals(gameData.whiteUsername())) {
                color = "white";
            } else if (username.equals(gameData.blackUsername())) {
                color = "black";
            } else {
                color = "an observer";
            }
            connections.sendSelf(session, new LoadGameMessage(gameData.game().toString()));

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

            if (game.isOver()) {
                connections.sendSelf(session, new ErrorMessage("Error: " + game.getGameOverReason()));
                return;
            }

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
                game.endGame();
                connections.broadcast(gameID, null,
                        new ServerMessage(NOTIFICATION, opponent + " is in checkmate!"));
            } else if (game.isInCheck(opponent)) {
                connections.broadcast(gameID, null,
                        new ServerMessage(NOTIFICATION, opponent + " is in check!"));
            } else if (game.isInStalemate(opponent)) {
                game.endGame();
                connections.broadcast(gameID, null,
                        new ServerMessage(NOTIFICATION, opponent + " is in stalemate!"));
            }

            dataAccess.updateGame(updatedGameData);
        } catch (DataAccessException ex) {
            connections.sendSelf(session, new ErrorMessage(ex.getMessage()));
        }
    }

    private void leave(Session session, UserGameCommand command) throws IOException {
        int gameID = command.getGameID();
        String authToken = command.getAuthToken();

        try {

            UserGameData data = authLoad(session, gameID, authToken);
            if (data == null) {return;}

            String username = data.username();
            GameData gameData = data.gameData();

            boolean changed = false;
            GameData updatedGame = null;

            if (username.equals(gameData.whiteUsername())) {
                updatedGame = new GameData(
                        gameData.gameID(),
                        null,
                        gameData.blackUsername(),
                        gameData.gameName(),
                        gameData.game()
                );
                changed = true;
            } else if (username.equals(gameData.blackUsername())) {
                updatedGame = new GameData(
                        gameData.gameID(),
                        gameData.whiteUsername(),
                        null,
                        gameData.gameName(),
                        gameData.game()
                );
                changed = true;
            }

            if (changed) {
                dataAccess.updateGame(updatedGame);
            }

            String message = username + " has left the game.";
            connections.broadcast(gameID, session,
                    new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message));

            connections.remove(gameID, session);
        } catch (DataAccessException ex) {
            connections.sendSelf(session, new ErrorMessage(ex.getMessage()));
        }
    }

    private void resign(Session session, UserGameCommand command) throws IOException {
        int gameID = command.getGameID();
        String authToken = command.getAuthToken();

        try {
            UserGameData data = authLoad(session, gameID, authToken);
            if (data == null) return;

            String username = data.username();
            GameData gameData = data.gameData();

            boolean isWhite = username.equals(gameData.whiteUsername());
            boolean isBlack = username.equals(gameData.blackUsername());

            // check if player
            if (!isWhite && !isBlack) {
                connections.sendSelf(session, new ErrorMessage("Error: Observers can't resign."));
                return;
            }

            ChessGame game = gameData.game();

            if (game.isOver()) {
                connections.sendSelf(session, new ErrorMessage("Error: " + game.getGameOverReason()));
                return;
            }

            ChessGame.TeamColor color = isWhite ? WHITE : BLACK;

            game.resign(color);

            dataAccess.updateGame(gameData);

            ServerMessage msg = new ServerMessage(NOTIFICATION, username + " resigned.");

            connections.broadcast(gameID, null, msg);
        } catch (DataAccessException ex) {
            connections.sendSelf(session, new ErrorMessage(ex.getMessage()));
        }
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

    private UserGameData authLoad(Session session, int gameID, String authToken) throws IOException {
        try {
            // authenticate
            AuthData auth = dataAccess.getAuth(authToken);
            if (auth == null) {
                connections.sendSelf(session, new ErrorMessage("Error: Unauthorized"));
                return null;
            }

            // load game
            GameData gameData = dataAccess.getGame(gameID);
            if (gameData == null) {
                connections.sendSelf(session, new ErrorMessage("Error: Game not found"));
                return null;
            }

            return new UserGameData(auth.username(), gameData);
        } catch (DataAccessException ex) {
            connections.sendSelf(session, new ErrorMessage(ex.getMessage()));
            return null;
        }
    }

    private record UserGameData(String username, GameData gameData) {}
}