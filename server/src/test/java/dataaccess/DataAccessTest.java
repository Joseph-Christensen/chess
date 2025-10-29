package dataaccess;

import model.UserData;
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
        UserData selectedUser = db.getUser("joe");

        assertNotNull(selectedUser);
        assertEquals(user.username(), selectedUser.username());
        assertEquals(user.password(), selectedUser.password());
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
        UserData selectedUser = db.getUser("joe");

        assertNotNull(selectedUser);
        assertEquals(user.username(), selectedUser.username());
        assertEquals(user.password(), selectedUser.password());
    }

    @Test
    void getUserFails() throws DataAccessException {
        UserData user = db.getUser("joe");
        assertNull(user);
    }
}