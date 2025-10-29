package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class GameService {

    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public HashSet<GameRepresentation> listGames(String authToken) throws ChessException {
        if (notAuthorized(authToken)) {
            throw new ChessException(401, "unauthorized");
        }
        var allGameData = dataAccess.allGames();
        HashSet<GameRepresentation> games = new HashSet<>();
        for (Map.Entry<Integer, GameData> entry : allGameData.entrySet()) {
            GameRepresentation currGame = new GameRepresentation(
                    entry.getValue().gameID(),
                    entry.getValue().whiteUsername(),
                    entry.getValue().blackUsername(),
                    entry.getValue().gameName()
            );
            games.add(currGame);
        }
        return games;
    }

    public CreateResponse createGame(GameEntry gameEntry, String authToken) throws ChessException {
        if (notAuthorized(authToken)) {
            throw new ChessException(401, "unauthorized");
        }
        if (gameEntry.gameName() == null || gameEntry.gameName().isBlank()) {
            throw new ChessException(400, "bad request");
        }
        GameData game = dataAccess.createGame(gameEntry.gameName());

        return new CreateResponse(game.gameID());
    }

    public void joinGame(JoinRequest joinRequest, String authToken) throws ChessException {
        try {
            if (notAuthorized(authToken)) {
                throw new ChessException(401, "unauthorized");
            }
            if (!Objects.equals(joinRequest.playerColor(), "WHITE")) {
                if (!Objects.equals(joinRequest.playerColor(), "BLACK")) {
                    throw new ChessException(400, "bad request");
                }
            }
            if (dataAccess.getGame(joinRequest.gameID()) == null) {
                throw new ChessException(400, "bad request");
            }
            boolean isWhite;
            if (joinRequest.playerColor().equals("WHITE")) {
                if (dataAccess.getGame(joinRequest.gameID()).whiteUsername() != null) {
                    throw new ChessException(403, "already taken");
                }
                isWhite = true;
            }
            else {
                if (dataAccess.getGame(joinRequest.gameID()).blackUsername() != null) {
                    throw new ChessException(403, "already taken");
                }
                isWhite = false;
            }
            String username = dataAccess.getUsername(authToken);
            dataAccess.updateGame(username, isWhite, joinRequest.gameID());
        } catch (DataAccessException ex) {
            throw new ChessException(500, ex.getMessage());
        }
    }

    private boolean notAuthorized(String authToken) throws ChessException {
        try {
            return !dataAccess.allAuths().containsKey(authToken);
        } catch (DataAccessException ex) {
            throw new ChessException(500, ex.getMessage());
        }
    }
}
