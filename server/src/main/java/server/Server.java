package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.SqlDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import model.*;
import service.ChessException;
import service.GameService;
import service.UserService;

import java.util.HashSet;
import java.util.Map;

public class Server {

    private final Javalin server;
    private final UserService userService;
    private final GameService gameService;

    public Server() {
        DataAccess dataAccess = null;
        try {
            dataAccess = new SqlDataAccess();
        } catch (DataAccessException ex) {
            var msg = String.format("{ \"message\": \"Error: %s\"}", ex.getMessage());
            System.out.print(msg);
            System.exit(400);
        }

        userService = new UserService(dataAccess);
        gameService = new GameService(dataAccess);

        server = Javalin.create(config -> config.staticFiles.add("web"));

        server.delete("db", this::clear);
        server.post("user", this::register);
        server.post("session", this::login);
        server.delete("session", this::logout);
        server.get("game", this::listGames);
        server.post("game", this::createGame);
        server.put("game", this::joinGame);
    }

    private void clear(Context ctx) {
        try {
            userService.clear();
            ctx.result("{}");
        } catch (ChessException ex) {
            var msg = String.format("{ \"message\": \"Error: %s\"}", ex.getMessage());
            ctx.status(ex.getCode()).result(msg);
        }
    }

    private void register(Context ctx) {
        try {
            var serializer = new Gson();
            String reqJson = ctx.body();
            var user = serializer.fromJson(reqJson, UserData.class);

            var authData = userService.register(user);

            ctx.result(serializer.toJson(authData));
        } catch (ChessException ex) {
            var msg = String.format("{ \"message\": \"Error: %s\"}", ex.getMessage());
            ctx.status(ex.getCode()).result(msg);
        }
    }

    private void login(Context ctx) {
        try {
            var serializer = new Gson();
            String reqJson = ctx.body();
            var user = serializer.fromJson(reqJson, LoginInfo.class);

            var authData = userService.login(user);

            ctx.result(serializer.toJson(authData));
        } catch (ChessException ex) {
            var msg = String.format("{ \"message\": \"Error: %s\"}", ex.getMessage());
            ctx.status(ex.getCode()).result(msg);
        }
    }

    private void logout(Context ctx) {
        try {
            String authToken = ctx.header("authorization");

            userService.logout(authToken);

            ctx.result("{}");
        } catch (ChessException ex) {
            var msg = String.format("{ \"message\": \"Error: %s\"}", ex.getMessage());
            ctx.status(ex.getCode()).result(msg);
        }
    }

    private void listGames(Context ctx) {
        try {
            var serializer = new Gson();

            String authToken = ctx.header("authorization");

            HashSet<GameRepresentation> games = gameService.listGames(authToken);

            ctx.result(serializer.toJson(Map.of("games", games)));
        } catch (ChessException ex) {
            var msg = String.format("{ \"message\": \"Error: %s\"}", ex.getMessage());
            ctx.status(ex.getCode()).result(msg);
        }
    }

    private void createGame(Context ctx) {
        try {
            var serializer = new Gson();
            String reqJson = ctx.body();
            var gameEntry = serializer.fromJson(reqJson, GameEntry.class);

            String authToken = ctx.header("authorization");

            CreateResponse res = gameService.createGame(gameEntry, authToken);

            ctx.result(serializer.toJson(res));
        } catch (ChessException ex) {
            var msg = String.format("{ \"message\": \"Error: %s\"}", ex.getMessage());
            ctx.status(ex.getCode()).result(msg);
        }
    }

    private void joinGame(Context ctx) {
        try {
            var serializer = new Gson();
            String reqJson = ctx.body();
            var joinRequest = serializer.fromJson(reqJson, JoinRequest.class);

            String authToken = ctx.header("authorization");

            gameService.joinGame(joinRequest, authToken);

            ctx.result("{}");
        } catch (ChessException ex) {
            var msg = String.format("{ \"message\": \"Error: %s\"}", ex.getMessage());
            ctx.status(ex.getCode()).result(msg);
        }
    }

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
