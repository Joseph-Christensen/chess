package client;

import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

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
}
