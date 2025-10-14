package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData user) throws Exception {
        if (dataAccess.getUser(user.username()) != null) {
            throw new Exception("already exists");
        }
        dataAccess.createUser(user);
        return new AuthData(user.username(), generateAuthToken());
    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}


