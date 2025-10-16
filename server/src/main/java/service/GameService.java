package service;

import dataaccess.DataAccess;
import model.AuthData;
import model.CreateResponse;
import model.GameData;
import model.GameEntry;

import java.util.HashMap;
import java.util.Map;

public class GameService {

    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public HashMap<Integer, GameData> listGames(String authToken) throws Exception {
        if (notAuthorized(authToken)) {
            throw new Exception("unauthorized");
        }
        return dataAccess.allGames();
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
