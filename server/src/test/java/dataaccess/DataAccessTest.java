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
        assertEquals("joe", selectedUser.username());
        assertEquals("manysecrets", selectedUser.password());
    }

    @Test
    void createUserFails() throws DataAccessException {

    }

    @Test
    void getUser() {
    }
}