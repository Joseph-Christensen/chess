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
    public void removeAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "DELETE FROM authData WHERE authToken = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException("Error removing auth: " + ex.getMessage(), ex);
        }
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username FROM authData WHERE authToken = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var res = ps.executeQuery()) {
                    if (res.next()) {
                        return res.getString("username");
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
    public String getPassword(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT password FROM userData WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var res = ps.executeQuery()) {
                    if (res.next()) {
                        return res.getString("password");
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
    public HashMap<String, AuthData> allAuths() throws DataAccessException {
        var auths = new HashMap<String, AuthData>();

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT authToken, username FROM authData";
            try (var ps = conn.prepareStatement(statement); var res = ps.executeQuery()) {
                while (res.next()) {
                    String token = res.getString("authToken");
                    String username = res.getString("username");

                    auths.put(token, new AuthData(token, username));
                }
            }
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException("Error retrieving all auths: " + ex.getMessage(), ex);
        }
        return auths;
    }

    @Override
    public GameData getGame(int id) throws DataAccessException {
        return null;
    }

    @Override
    public HashMap<Integer, GameData> allGames() throws DataAccessException {
        return null;
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(String username, boolean isWhite, int id) throws DataAccessException {

    }
}
