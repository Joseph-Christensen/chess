package dataaccess;

import model.*;

import java.util.HashMap;

public interface DataAccess {
    void clear();
    void createUser(UserData user);
    UserData getUser(String username);
    void createAuth(AuthData user);
    AuthData getAuth(String username);
    void removeAuth(String username);
    String getPassword(String username);
    HashMap<String,AuthData> allAuths();
    GameData getGame(int id);
    HashMap<Integer, GameData> allGames();
    GameData createGame(String gameName);
}
