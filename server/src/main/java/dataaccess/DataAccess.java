package dataaccess;

import model.*;

import java.util.HashMap;

public interface DataAccess {
    void clear() throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void createAuth(AuthData user) throws DataAccessException;
    AuthData getAuth(String username) throws DataAccessException;
    void removeAuth(String username) throws DataAccessException;
    String getUsername(String authToken);
    String getPassword(String username);
    HashMap<String,AuthData> allAuths() throws DataAccessException;
    GameData getGame(int id);
    HashMap<Integer, GameData> allGames();
    GameData createGame(String gameName);
    void updateGame(String username, boolean isWhite, int id);
}
