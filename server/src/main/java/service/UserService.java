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

    public AuthData register(UserData user) throws ChessException {
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new ChessException(400, "bad request");
        }
        if (dataAccess.getUser(user.username()) != null) {
            throw new ChessException(403, "already taken");
        }
        dataAccess.createUser(user);
        dataAccess.createAuth(new AuthData(user.username(), generateAuthToken()));
        return dataAccess.getAuth(user.username());
    }

    public AuthData login(LoginInfo user) throws ChessException {
        if (user.username() == null || user.password() == null) {
            throw new ChessException(400, "bad request");
        }
        if (dataAccess.getUser(user.username()) == null) {
            throw new ChessException(401, "unauthorized");
        }
        if (!dataAccess.getPassword(user.username()).equals(user.password())) {
            throw new ChessException(401, "unauthorized");
        }
        dataAccess.createAuth(new AuthData(user.username(), generateAuthToken()));
        return dataAccess.getAuth(user.username());
    }

    public void logout(String authToken) throws ChessException {
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
            throw new ChessException(401, "unauthorized");
        }
    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}


