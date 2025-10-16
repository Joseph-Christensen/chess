package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void clear() {
    }

    @Test
    void register() throws ChessException {
        DataAccess db = new MemoryDataAccess();
        UserService service = new UserService(db);
        UserData user = new UserData("joe", "j@j.com", "manysecrets");
        var authData = service.register(user);
        assertNotNull(authData);
        assertEquals(user.username(), authData.username());
        assertFalse(authData.authToken().isEmpty());
    }

    @Test
    void registerInvalidUsername() throws ChessException {
        DataAccess db = new MemoryDataAccess();
        UserService service = new UserService(db);
        UserData user = new UserData("", "j@j.com", "manysecrets");
//      assertThrows(ChessException)
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }
}