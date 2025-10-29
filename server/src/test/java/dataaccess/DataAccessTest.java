package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessTest {

    @Test
    void clear() throws DataAccessException {
        DataAccess db = new MemoryDataAccess();
        db.createUser(new UserData("joe", "manysecrets", "j@j.com"));
        db.clear();
        assertNull(db.getUser("joe"));
    }

    @Test
    void createUser() {
        DataAccess db = new MemoryDataAccess();
        UserData user = new UserData("joe", "manysecrets", "j@j.com");
        db.createUser(user);
        assertEquals(user, db.getUser(user.username()));
    }

    @Test
    void getUser() {
    }
}