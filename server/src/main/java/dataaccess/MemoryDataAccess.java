package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.HashMap;
import java.util.Map;

public class MemoryDataAccess implements DataAccess {

    private final HashMap<String, UserData> users = new HashMap<>();
    private final HashMap<String, AuthData> auths = new HashMap<>();
    private final HashMap<Integer, GameData> games = new HashMap<>();
    private int nextID = 1;

    @Override
    public void clear() {
        users.clear();
        auths.clear();
        games.clear();
        nextID = 1;
    }

    @Override
    public void createUser(UserData user) {
        users.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public void createAuth(AuthData user) {
        auths.put(user.authToken(), user);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }

    @Override
    public void removeAuth(String authToken) {
        auths.remove(authToken);
    }

    @Override
    public String getUsername(String authToken) {
        var myAuths = allAuths();
        for (Map.Entry<String, AuthData> entry : myAuths.entrySet()) {
            if (entry.getValue().authToken().equals(authToken)) {
                return entry.getValue().username();
            }
        }
        return null;
    }

    @Override
    public String getPassword(String username) {
        return getUser(username).password();
    }

    @Override
    public HashMap<String, AuthData> allAuths() {
        return auths;
    }

    @Override
    public GameData createGame(String gameName) {
        GameData game = new GameData(nextID, null, null, gameName, new ChessGame());
        games.put(nextID, game);
        nextID++;
        return game;
    }

    @Override
    public GameData getGame(int id) {
        return games.get(id);
    }

    @Override
    public HashMap<Integer, GameData> allGames() {
        return games;
    }

    @Override
    public void updateGame(String username, boolean isWhite, int id) {
        GameData selectedGame = getGame(id);
        if (isWhite) {
            games.put(id, new GameData(
                    selectedGame.gameID(),
                    username,
                    selectedGame.blackUsername(),
                    selectedGame.gameName(),
                    selectedGame.game()
            ));
        }
        else {
            games.put(id, new GameData(
                    selectedGame.gameID(),
                    selectedGame.whiteUsername(),
                    username,
                    selectedGame.gameName(),
                    selectedGame.game()
            ));
        }
    }
}
