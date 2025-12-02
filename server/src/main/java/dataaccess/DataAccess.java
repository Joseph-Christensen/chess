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
    String getUsername(String authToken) throws DataAccessException;
    String getPassword(String username) throws DataAccessException;
    HashMap<String,AuthData> allAuths() throws DataAccessException;
    GameData createGame(String gameName) throws DataAccessException;
    GameData getGame(int id) throws DataAccessException;
    HashMap<Integer, GameData> allGames() throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
}
