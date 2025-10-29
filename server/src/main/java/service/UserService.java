package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear() throws ChessException {
        try {
            dataAccess.clear();
        } catch (DataAccessException ex) {
            throw new ChessException(500, ex.getMessage());
        }

    }

    public AuthData register(UserData user) throws ChessException {
        if (
            user == null ||
            user.username() == null || user.username().isBlank() ||
            user.password() == null || user.password().isBlank() ||
            user.email() == null || user.email().isBlank()
        ) {
            throw new ChessException(400, "bad request");
        }
        try {
            if (dataAccess.getUser(user.username()) != null) {
                throw new ChessException(403, "already taken");
            }
            UserData hashedUser = new UserData(user.username(), hashPassword(user.password()), user.email());
            dataAccess.createUser(hashedUser);
            String authToken = generateAuthToken();
            dataAccess.createAuth(new AuthData(authToken, user.username()));
            return dataAccess.getAuth(authToken);
        }
        catch (DataAccessException ex) {
            throw new ChessException(500, ex.getMessage());
        }
    }

    public AuthData login(LoginInfo user) throws ChessException {
        if (
            user == null ||
            user.username() == null || user.username().isBlank() ||
            user.password() == null || user.password().isBlank()
        ) {
            throw new ChessException(400, "bad request");
        }
        try {
            if (dataAccess.getUser(user.username()) == null) {
                throw new ChessException(401, "unauthorized");
            }
            if (!verifyPassword(user.username(), user.password())) {
                throw new ChessException(401, "unauthorized");
            }
            String authToken = generateAuthToken();
            dataAccess.createAuth(new AuthData(authToken, user.username()));
            return dataAccess.getAuth(authToken);
        } catch (DataAccessException ex) {
            throw new ChessException(500, ex.getMessage());
        }

    }

    public void logout(String authToken) throws ChessException {
        try {
            if (notAuthorized(authToken)) {
                throw new ChessException(401, "unauthorized");
            }
            dataAccess.removeAuth(authToken);
        } catch (DataAccessException ex) {
            throw new ChessException(500, ex.getMessage());
        }
    }

    private boolean notAuthorized(String authToken) throws ChessException {
        try {
            return !dataAccess.allAuths().containsKey(authToken);
        } catch (DataAccessException ex) {
            throw new ChessException(500, ex.getMessage());
        }
    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    boolean verifyPassword(String username, String password) throws DataAccessException {
        return BCrypt.checkpw(password, dataAccess.getPassword(username));
    }
}


