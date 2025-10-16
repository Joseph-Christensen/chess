package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.LoginInfo;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void clear() throws ChessException {
        DataAccess db = new MemoryDataAccess();
        UserService service = new UserService(db);
        UserData user = new UserData("joe", "manysecrets", "j@j.com");
        service.register(user);
        service.clear();

        ChessException ex = assertThrows(
                ChessException.class,
                () -> service.login(new LoginInfo(user.username(), user.password()))
        );

        assertEquals(401, ex.getCode());
        assertEquals("unauthorized", ex.getMessage());
    }

    @Test
    void register() throws ChessException {
        DataAccess db = new MemoryDataAccess();
        UserService service = new UserService(db);
        UserData user = new UserData("joe", "manysecrets", "j@j.com");
        var authData = service.register(user);
        assertNotNull(authData);
        assertEquals(user.username(), authData.username());
        assertFalse(authData.authToken().isEmpty());
    }

    @Test
    void registerInvalidUsername() {
        DataAccess db = new MemoryDataAccess();
        UserService service = new UserService(db);
        UserData user = new UserData("", "manysecrets", "j@j.com");

        ChessException ex = assertThrows(
                ChessException.class,
                () -> service.register(user)
        );

        assertEquals(400, ex.getCode());
        assertEquals("bad request", ex.getMessage());

        UserData nextUser = new UserData(null, "somesecrets", "m@m.com");

        ChessException nextEx = assertThrows(
                ChessException.class,
                () -> service.register(nextUser)
        );

        assertEquals(400, nextEx.getCode());
        assertEquals("bad request", nextEx.getMessage());
    }

    @Test
    void login() throws ChessException {
        DataAccess db = new MemoryDataAccess();
        UserService service = new UserService(db);
        UserData user = new UserData("joe", "manysecrets", "j@j.com");
        service.register(user);
        var authData = service.login(new LoginInfo(user.username(), user.password()));
        assertNotNull(authData);
        assertEquals(user.username(), authData.username());
        assertFalse(authData.authToken().isEmpty());
    }

    @Test
    void loginIncorrectInfo() throws ChessException {
        DataAccess db = new MemoryDataAccess();
        UserService service = new UserService(db);
        UserData user = new UserData("joe", "manysecrets", "j@j.com");
        service.register(user);

        ChessException ex = assertThrows(
                ChessException.class,
                () -> service.login(new LoginInfo("jae", user.password()))
        );

        assertEquals(401, ex.getCode());
        assertEquals("unauthorized", ex.getMessage());

        ChessException nextEx = assertThrows(
                ChessException.class,
                () -> service.login(new LoginInfo(user.username(), "somesecrets"))
        );

        assertEquals(401, nextEx.getCode());
        assertEquals("unauthorized", nextEx.getMessage());
    }

    @Test
    void logout() throws ChessException {
        DataAccess db = new MemoryDataAccess();
        UserService service = new UserService(db);
        UserData user = new UserData("joe", "manysecrets", "j@j.com");
        var authData = service.register(user);
        service.logout(authData.authToken());

        ChessException ex = assertThrows(
                ChessException.class,
                () -> service.login(new LoginInfo("jae", user.password()))
        );

        assertEquals(401, ex.getCode());
        assertEquals("unauthorized", ex.getMessage());
    }

    @Test
    void logoutWrongAuth() throws ChessException {
        DataAccess db = new MemoryDataAccess();
        UserService service = new UserService(db);
        UserData user = new UserData("joe", "manysecrets", "j@j.com");
        service.register(user);

        ChessException ex = assertThrows(
                ChessException.class,
                () -> service.logout("MyAuthToken")
        );

        assertEquals(401, ex.getCode());
        assertEquals("unauthorized", ex.getMessage());
    }
}