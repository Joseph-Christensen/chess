package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import model.*;
import service.GameService;
import service.UserService;

import java.util.Map;

public class Server {

    private final Javalin server;
    private final UserService userService;
    private final GameService gameService;

    public Server() {
        DataAccess dataAccess = new MemoryDataAccess();
        userService = new UserService(dataAccess);
        gameService = new GameService(dataAccess);

        server = Javalin.create(config -> config.staticFiles.add("web"));

        server.delete("db", ctx -> clear(ctx));
        server.post("user", ctx -> register(ctx));
        server.post("session", ctx -> login(ctx));
        server.delete("session", ctx -> logout(ctx));
        server.get("game", ctx -> listGames(ctx));
        server.post("game", ctx -> createGame(ctx));
        server.put("game", ctx -> joinGame(ctx));
    }

    private void clear(Context ctx) {
        userService.clear();
        ctx.result("{}");
    }

    private void register(Context ctx) {
        try {
            var serializer = new Gson();
            String reqJson = ctx.body();
            var user = serializer.fromJson(reqJson, UserData.class);

            var authData = userService.register(user);

            ctx.result(serializer.toJson(authData));
        } catch (Exception ex) {
            var msg = String.format("{ \"message\": \"Error: %s\"}", ex.getMessage());
            ctx.status(403).result(msg);
        }
    }

    private void login(Context ctx) {
        try {
            var serializer = new Gson();
            String reqJson = ctx.body();
            var user = serializer.fromJson(reqJson, LoginInfo.class);

            var authData = userService.login(user);

            ctx.result(serializer.toJson(authData));
        } catch (Exception ex) {
            var msg = String.format("{ \"message\": \"Error: %s\"}", ex.getMessage());
            ctx.status(401).result(msg);
        }
    }

    private void logout(Context ctx) {
        try {
            String authToken = ctx.header("authorization");

            userService.logout(authToken);

            ctx.result("{}");
        } catch (Exception ex) {
            var msg = String.format("{ \"message\": \"Error: %s\"}", ex.getMessage());
            ctx.status(401).result(msg);
        }
    }

    private void listGames(Context ctx) {
        try {
            var serializer = new Gson();

            String authToken = ctx.header("authorization");

            var games = gameService.listGames(authToken);

            ctx.result(serializer.toJson(games));
        } catch (Exception ex) {
            var msg = String.format("{ \"message\": \"Error: %s\"}", ex.getMessage());
            ctx.status(401).result(msg);
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
        } catch (Exception ex) {
            var msg = String.format("{ \"message\": \"Error: %s\"}", ex.getMessage());
            ctx.status(401).result(msg);
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
        } catch (Exception ex) {
            var msg = String.format("{ \"message\": \"Error: %s\"}", ex.getMessage());
            ctx.status(401).result(msg);
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
