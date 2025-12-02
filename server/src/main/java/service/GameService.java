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
        try {
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
        } catch (DataAccessException ex) {
            throw new ChessException(500, ex.getMessage());
        }
    }

    public CreateResponse createGame(GameEntry gameEntry, String authToken) throws ChessException {
        try {
            if (notAuthorized(authToken)) {
                throw new ChessException(401, "unauthorized");
            }
            if (gameEntry.gameName() == null || gameEntry.gameName().isBlank()) {
                throw new ChessException(400, "bad request");
            }
            GameData game = dataAccess.createGame(gameEntry.gameName());

            return new CreateResponse(game.gameID());
        } catch (DataAccessException ex) {
            throw new ChessException(500, ex.getMessage());
        }
    }

    public void joinGame(JoinRequest joinRequest, String authToken) throws ChessException {
        try {
            if (notAuthorized(authToken)) {
                throw new ChessException(401, "unauthorized");
            }
            String color = joinRequest.playerColor();
            int gameID = joinRequest.gameID();
            if (!Objects.equals(color, "WHITE")) {
                if (!Objects.equals(color, "BLACK")) {
                    throw new ChessException(400, "bad request");
                }
            }
            GameData game = dataAccess.getGame(gameID);
            if (game == null) {
                throw new ChessException(400, "bad request");
            }
            boolean isWhite;
            if (color.equals("WHITE")) {
                if (game.whiteUsername() != null) {
                    throw new ChessException(403, "already taken");
                }
                isWhite = true;
            }
            else {
                if (game.blackUsername() != null) {
                    throw new ChessException(403, "already taken");
                }
                isWhite = false;
            }
            String username = dataAccess.getUsername(authToken);
            GameData newData;
            if (isWhite) {
                newData = new GameData(gameID, username, game.blackUsername(), game.gameName(), game.game());
            } else {
                newData = new GameData(gameID, game.whiteUsername(), username, game.gameName(), game.game());
            }
            dataAccess.updateGame(newData);
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
