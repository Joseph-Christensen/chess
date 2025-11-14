package client;

import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearServer() throws Exception {
        facade.clear();
    }

    @Test
    public void clear() throws Exception {
        UserData user = new UserData("joe", "secrets", "j@j.com");
        facade.register(user);

        facade.clear();

        assertThrows(ResponseException.class, () ->
                facade.login(new LoginInfo(user.username(), user.password()))
        );
    }

    @Test
    public void register() throws Exception {
        UserData user = new UserData("joe", "secrets", "j@j.com");
        AuthData auth = facade.register(user);
        assertEquals("joe", auth.username());
    }

    @Test
    public void registerNegative() throws Exception {
        UserData user = new UserData("joe", "secrets", "j@j.com");
        facade.register(user);

        assertThrows(ResponseException.class, () ->
                facade.register(user)
        );
    }

    @Test
    public void login() throws Exception {
        UserData user = new UserData("joe", "secrets", "j@j.com");
        facade.register(user);

        LoginInfo login = new LoginInfo("joe", "secrets");
        AuthData auth = facade.login(login);

        assertEquals("joe", auth.username());
    }

    @ Test
    public void loginNegative() throws Exception {
        UserData user = new UserData("joe", "secrets", "j@j.com");
        facade.register(user);

        LoginInfo login = new LoginInfo("joe", "openbook");

        assertThrows(ResponseException.class, () ->
                facade.login(login)
        );
    }

    @Test
    public void logout() throws Exception {
        UserData user = new UserData("joe", "secrets", "j@j.com");
        AuthData auth = facade.register(user);

        assertDoesNotThrow(() -> facade.logout(auth.authToken()));
    }

    @Test
    public void logoutNegative() {
        assertThrows(ResponseException.class, () ->
                facade.logout("xyz")
        );
    }

    @Test
    public void createGamePositive() throws Exception {
        UserData user = new UserData("joe", "secrets", "j@j.com");
        AuthData auth = facade.register(user);

        GameEntry entry = new GameEntry("myGame");
        assertDoesNotThrow(() -> facade.createGame(entry, auth.authToken()));
    }

    @Test
    public void createGameNegative() {
        GameEntry entry = new GameEntry("myGame");

        assertThrows(ResponseException.class, () ->
                facade.createGame(entry, "xyz")
        );
    }

    @Test
    public void listGames() throws Exception {
        UserData user = new UserData("joe", "secrets", "j@j.com");
        AuthData auth = facade.register(user);

        facade.createGame(new GameEntry("myGame"), auth.authToken());
        HashSet<GameRepresentation> games = facade.listGames(auth.authToken());

        assertFalse(games.isEmpty());
    }

    @Test
    public void listGamesNegative() {
        assertThrows(ResponseException.class, () ->
                facade.listGames("xyz")
        );
    }
}
