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
        String authToken = generateAuthToken();
        dataAccess.createAuth(new AuthData(authToken, user.username()));
        return dataAccess.getAuth(authToken);
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
        String authToken = generateAuthToken();
        dataAccess.createAuth(new AuthData(authToken, user.username()));
        return dataAccess.getAuth(authToken);
    }

    public void logout(String authToken) throws ChessException {
        if (notAuthorized(authToken)) {
            throw new ChessException(401, "unauthorized");
        }
        dataAccess.removeAuth(authToken);
    }

    private boolean notAuthorized(String authToken) {
        return !dataAccess.allAuths().containsKey(authToken);
    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}


