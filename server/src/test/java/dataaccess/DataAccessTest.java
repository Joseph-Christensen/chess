package dataaccess;

import model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        db.createUser(user);
        UserData found = db.getUser("joe");

        assertNotNull(found);
        assertEquals(user.username(), found.username());
        assertEquals(user.password(), found.password());
    }

    @Test
    void createUserFails() throws DataAccessException {
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
    void getUserFails() throws DataAccessException {
        UserData user = db.getUser("joe");

        assertNull(user);
    }

    @Test
    void createAuth() throws DataAccessException {
        AuthData auth = new AuthData("xyz", "joe");
        db.createAuth(auth);
        AuthData found = db.getAuth("xyz");

        assertNotNull(found);
        assertEquals(auth.username(), found.username());
    }

    @Test
    void createAuthFails() throws DataAccessException {
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
    void getAuthFails() throws DataAccessException {
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
    public void removeAuthFails() throws DataAccessException {
        assertDoesNotThrow(() -> db.removeAuth("xyz"));
    }

    @Test
    void getUsername() throws DataAccessException {
        AuthData auth = new AuthData("xyz", "joe");
        db.createAuth(auth);
        assertEquals(auth.username(), db.getUsername("xyz"));
    }

    @Test
    void getUsernameFails() throws DataAccessException {
        assertNull(db.getUsername("xyz"));
    }

    @Test
    void getPassword() throws DataAccessException {
        UserData user = new UserData("joe", "manysecrets", "j@j.com");
        db.createUser(user);
        assertEquals(user.password(), db.getPassword(user.username()));
    }

    @Test
    void getPasswordFails() throws DataAccessException {
        assertNull(db.getPassword("joe"));
    }
}