package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.HashMap;

public class SqlDataAccess implements DataAccess {

    public SqlDataAccess() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String[] statements = {"TRUNCATE TABLE userData", "TRUNCATE TABLE authData", "TRUNCATE TABLE gameData"};
            for (String statement : statements) {
                try (var ps = conn.prepareStatement(statement)) {
                    ps.executeUpdate();
                }
            }
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException("Error clearing tables: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO userData (username, password, email) VALUES (?,?,?)";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, user.username());
                ps.setString(2, user.password());
                ps.setString(3, user.email());
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException("Error creating user: " + ex.getMessage(), ex);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var ps = conn.prepareStatement("SELECT username, password, email FROM userData WHERE username = ?");
            ps.setString(1, username);
            try (var res = ps.executeQuery()) {
                if (res.next()) {
                    return new UserData(
                            res.getString("username"),
                            res.getString("password"),
                            res.getString("email")
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException("Error getting user: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void createAuth(AuthData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, user.authToken());
                ps.setString(2, user.username());
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException("Error creating auth: " + ex.getMessage(), ex);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT authToken, username FROM authData WHERE authToken = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var res = ps.executeQuery()) {
                    if (res.next()) {
                        return new AuthData(
                                res.getString("authToken"),
                                res.getString("username")
                        );
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException("Error getting auth: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void removeAuth(String authToken) {

    }

    @Override
    public String getUsername(String authToken) {
        return "";
    }

    @Override
    public String getPassword(String username) {
        return "";
    }

    @Override
    public HashMap<String, AuthData> allAuths() {
        return null;
    }

    @Override
    public GameData getGame(int id) {
        return null;
    }

    @Override
    public HashMap<Integer, GameData> allGames() {
        return null;
    }

    @Override
    public GameData createGame(String gameName) {
        return null;
    }

    @Override
    public void updateGame(String username, boolean isWhite, int id) {

    }
}
