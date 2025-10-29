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
            var statement = "DROP DATABASE chess";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void createUser(UserData user) {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void createAuth(AuthData user) {

    }

    @Override
    public AuthData getAuth(String username) {
        return null;
    }

    @Override
    public void removeAuth(String username) {

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
