package service;

import dataaccess.DataAccess;
import model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class GameService {

    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public HashSet<GameRepresentation> listGames(String authToken) throws Exception {
        if (notAuthorized(authToken)) {
            throw new Exception("unauthorized");
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

    public CreateResponse createGame(GameEntry gameEntry, String authToken) throws Exception {
        if (notAuthorized(authToken)) {
            throw new Exception("unauthorized");
        }
        GameData game = dataAccess.createGame(gameEntry.gameName());

        return new CreateResponse(game.gameID());
    }

    public void joinGame(JoinRequest joinRequest, String authToken) throws Exception {
        if (notAuthorized(authToken)) {
            throw new Exception("unauthorized");
        }
        if (!Objects.equals(joinRequest.playerColor(), "WHITE")) {
            if (!Objects.equals(joinRequest.playerColor(), "BLACK")) {
                throw new Exception("bad request");
            }
        }
        if (dataAccess.getGame(joinRequest.gameID()) == null) {
            throw new Exception("bad request");
        }
        boolean isWhite;
        if (joinRequest.playerColor().equals("WHITE")) {
            if (dataAccess.getGame(joinRequest.gameID()).whiteUsername() != null) {
                throw new Exception("already taken");
            }
            isWhite = true;
        }
        else {
            if (dataAccess.getGame(joinRequest.gameID()).blackUsername() != null) {
                throw new Exception("already taken");
            }
            isWhite = false;
        }
        String username = dataAccess.getUsername(authToken);
        dataAccess.updateGame(username, isWhite, joinRequest.gameID());
    }


    private boolean notAuthorized(String authToken) {
        var myAuths = dataAccess.allAuths();
        for (Map.Entry<String, AuthData> entry : myAuths.entrySet()) {
            if (entry.getValue().authToken().equals(authToken)) {
                return false;
            }
        }
        return true;
    }
}
