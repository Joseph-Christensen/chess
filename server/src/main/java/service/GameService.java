package service;

import dataaccess.DataAccess;
import model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
