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

        ChessException ex = assertThrows(
                ChessException.class,
                () -> service.register(user)
        );

        assertEquals(400, ex.getCode());
        assertEquals("bad request", ex.getMessage());

        UserData nextUser = new UserData(null, "j@j.com", "manysecrets");

        ChessException nextEx = assertThrows(
                ChessException.class,
                () -> service.register(nextUser)
        );

        assertEquals(400, nextEx.getCode());
        assertEquals("bad request", nextEx.getMessage());
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }
}