package dataaccess;

import model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessTest {

    static SqlDataAccess db;

    @BeforeAll
    static void setup() throws DataAccessException {
        db = new SqlDataAccess();
    }

    @BeforeEach
    void clear() throws DataAccessException {
        db.clear();
    }


    @Test
    void clearTest() throws DataAccessException {
        db.createUser(new UserData("joe", "manysecrets", "j@j.com"));
        db.clear();

        assertNull(db.getUser("joe"));
    }

    @Test
    void createUser() throws DataAccessException {
        UserData user = new UserData("joe", "manysecrets", "j@j.com");

        assertDoesNotThrow(() -> db.createUser(user));

        assertNotNull(db.getUser("joe"));
    }

    @Test
    void createUserNegative() throws DataAccessException {
        UserData user = new UserData("joe", "manysecrets", "j@j.com");
        db.createUser(user);

        assertThrows(
                DataAccessException.class,
                () -> db.createUser(new UserData(user.username(), "somesecrets", "john@johnson.com"))
        );
    }

    @Test
    void getUser() throws DataAccessException {
        UserData user = new UserData("joe", "manysecrets", "j@j.com");
        db.createUser(user);
        UserData found = db.getUser("joe");

        assertNotNull(found);
        assertEquals(user.username(), found.username());
        assertEquals(user.password(), found.password());
    }

    @Test
    void getUserNegative() throws DataAccessException {
        UserData user = db.getUser("joe");

        assertNull(user);
    }

    @Test
    void createAuth() throws DataAccessException {
        AuthData auth = new AuthData("xyz", "joe");

        assertDoesNotThrow(() -> db.createAuth(auth));

        assertNotNull(db.getAuth("xyz"));
    }

    @Test
    void createAuthNegative() throws DataAccessException {
        AuthData auth = new AuthData("xyz", "joe");
        db.createAuth(auth);

        assertThrows(
                DataAccessException.class,
                () -> db.createAuth(new AuthData(auth.authToken(), "john"))
        );
    }

    @Test
    void getAuth() throws DataAccessException {
        AuthData auth = new AuthData("xyz", "joe");
        db.createAuth(auth);
        AuthData found = db.getAuth("xyz");

        assertNotNull(found);
        assertEquals(auth.username(), found.username());
    }

    @Test
    void getAuthNegative() throws DataAccessException {
        assertNull(db.getAuth("xyz"));
    }

    @Test
    public void removeAuth() throws DataAccessException {
        AuthData auth = new AuthData("xyz", "joe");
        db.createAuth(auth);

        db.removeAuth(auth.authToken());
        AuthData result = db.getAuth("xyz");

        assertNull(result);
    }

    @Test
    public void removeAuthNegative() {
        assertDoesNotThrow(() -> db.removeAuth("xyz"));
    }

    @Test
    void getUsername() throws DataAccessException {
        AuthData auth = new AuthData("xyz", "joe");
        db.createAuth(auth);
        assertEquals(auth.username(), db.getUsername("xyz"));
    }

    @Test
    void getUsernameNegative() throws DataAccessException {
        assertNull(db.getUsername("xyz"));
    }

    @Test
    void getPassword() throws DataAccessException {
        UserData user = new UserData("joe", "manysecrets", "j@j.com");
        db.createUser(user);
        assertEquals(user.password(), db.getPassword(user.username()));
    }

    @Test
    void getPasswordNegative() throws DataAccessException {
        assertNull(db.getPassword("joe"));
    }

    @Test
    void allAuths() throws DataAccessException {
        db.createAuth(new AuthData("t1", "u1"));
        db.createAuth(new AuthData("t2", "u2"));
        HashMap<String, AuthData> auths = db.allAuths();
        assertEquals(2, auths.size());
    }

    @Test
    void allAuthsNegative() throws DataAccessException {
        assertTrue(db.allAuths().isEmpty());
    }

    @Test
    void createGame() throws DataAccessException {
        GameData game = db.createGame("game1");
        assertNotNull(game);
        assertEquals(1, game.gameID());
    }

    @Test
    void createGameNegative() {
        assertThrows(DataAccessException.class, () -> db.createGame(null));
    }

    @Test
    void getGame() throws DataAccessException {
        GameData game = db.createGame("game1");
        assertNotNull(game);
        GameData found = db.getGame(game.gameID());
        assertNotNull(found);
        assertEquals(game.gameName(), found.gameName());
    }

    @Test
    void getGameNegative() throws DataAccessException {
        assertNull(db.getGame(1));
    }

    @Test
    void allGames() throws DataAccessException {
        db.createGame("game1");
        db.createGame("game2");
        HashMap<Integer, GameData> all = db.allGames();
        assertTrue(all.size() >= 2);
    }

    @Test
    void allGamesNegative() throws DataAccessException {
        assertTrue(db.allGames().isEmpty());
    }

    @Test
    void updateGame() throws DataAccessException {
        GameData game = db.createGame("game1");
        UserData user = new UserData("joe", "manysecrets", "j@j.com");
        db.updateGame(user.username(), true, game.gameID());
        GameData found = db.getGame(game.gameID());
        assertEquals(user.username(), found.whiteUsername());
    }

    @Test
    void updateGameNegative() throws DataAccessException {
        GameData game = db.createGame("game1");
        UserData user = new UserData("joe", "manysecrets", "j@j.com");

        assertDoesNotThrow(() -> db.updateGame(user.username(), true, 2));
        assertNull(db.getGame(game.gameID()).whiteUsername());
    }
}