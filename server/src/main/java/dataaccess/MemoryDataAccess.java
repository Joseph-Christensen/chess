package dataaccess;

import model.*;

import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {

    private final HashMap<String, UserData> users = new HashMap<>();
    private final HashMap<String, AuthData> auths = new HashMap<>();
    private final HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
        auths.clear();
        games.clear();
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
        auths.put(user.username(), user);
    }

    @Override
    public AuthData getAuth(String username) {
        return auths.get(username);
    }

    @Override
    public void removeAuth(String username) {
        auths.remove(username);
    }

    @Override
    public String getPassword(String username) {
        return getUser(username).password();
    }

    @Override
    public HashMap<String, AuthData> allAuths() {
        return auths;
    }


}
