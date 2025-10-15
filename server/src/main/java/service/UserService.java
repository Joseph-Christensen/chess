package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;

import java.util.Map;
import java.util.UUID;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear() {
        dataAccess.clear();
    }

    public AuthData register(UserData user) throws Exception {
        if (dataAccess.getUser(user.username()) != null) {
            throw new Exception("already exists");
        }
        dataAccess.createUser(user);
        dataAccess.createAuth(new AuthData(user.username(), generateAuthToken()));
        return dataAccess.getAuth(user.username());
    }

    public AuthData login(LoginInfo user) throws Exception {
        if (dataAccess.getUser(user.username()) == null) {
            throw new Exception("username not found");
        }
        if (!dataAccess.getPassword(user.username()).equals(user.password())) {
            throw new Exception("incorrect password");
        }
        dataAccess.createAuth(new AuthData(user.username(), generateAuthToken()));
        return dataAccess.getAuth(user.username());
    }

    public void logout(String authToken) throws Exception {
        boolean match = false;
        var myAuths = dataAccess.allAuths();
        for (Map.Entry<String, AuthData> entry : myAuths.entrySet()) {
            if (entry.getValue().authToken().equals(authToken)) {
                match = true;
                dataAccess.removeAuth(entry.getKey());
                break; // stop once found
            }
        }
        if (!match) {
            throw new Exception("unauthorized");
        }
    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}


