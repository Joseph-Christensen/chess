package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;
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

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}


