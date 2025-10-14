package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;

import java.util.Map;

public class Server {

    private final Javalin server;

    public Server() {
        server = Javalin.create(config -> config.staticFiles.add("web"));

        server.delete("db", ctx -> ctx.result("{}"));

        server.post("user", ctx -> register(ctx));

        server.post("session", ctx -> login(ctx));
    }

    private void register(Context ctx) {
        var serializer = new Gson();
        String reqJson = ctx.body();
        // String auth = ctx.header();
        var req = serializer.fromJson(reqJson, UserData.class);

        // call to the service and register

        var res = Map.of("username", req.username(), "authToken", "yzx");
        ctx.result(serializer.toJson(res));
    }

    private void login(Context ctx) {
        var serializer = new Gson();
        String reqJson = ctx.body();
        var req = serializer.fromJson(reqJson, AuthData.class);

        var res = Map.of("username", req.username(), "authToken", "xyz");
        ctx.result(serializer.toJson(res));
    }

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
