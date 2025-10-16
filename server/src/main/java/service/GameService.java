package service;

import dataaccess.DataAccess;
import model.AuthData;
import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class GameService {

    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public HashMap<Integer, GameData> listGames(String authToken) throws Exception {
        if (!authorize(authToken)) {
            throw new Exception("unauthorized");
        }
        return dataAccess.allGames();
    }


    private boolean authorize(String authToken) {
        var myAuths = dataAccess.allAuths();
        for (Map.Entry<String, AuthData> entry : myAuths.entrySet()) {
            if (entry.getValue().authToken().equals(authToken)) {
                return true;
            }
        }
        return false;
    }
}
