package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exception.ResponseException;
import model.*;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.lang.reflect.Type;
import java.util.HashSet;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private final Gson gson = new Gson();

    public ServerFacade(String url) {
        serverUrl = url;
    }

    // Put Methods Here

    public AuthData register(UserData user) throws ResponseException {
        var request = buildRequest("POST", "/user", user, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public AuthData login(LoginInfo loginInfo) throws ResponseException {
        var request = buildRequest("POST", "/session", loginInfo, null);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public void logout(String authToken) throws ResponseException {
        var request = buildRequest("DELETE", "/session", null, authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public void createGame(GameEntry entry, String authToken) throws ResponseException {
        var request = buildRequest("POST", "/game", entry, authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public HashSet<GameRepresentation> listGames(String authToken) throws ResponseException {
        var request = buildRequest("GET", "/game", null, authToken);
        var response = sendRequest(request);
        GameListResponse games = handleResponse(response, GameListResponse.class);
        return (games != null) ? games.getGames() : null;
    }

    private HttpRequest buildRequest(String method, String path, Object body, Object auth) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (auth != null) {
            request.setHeader("Authorization", auth.toString());
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        int status = response.statusCode();
        String body = response.body();

        if (isSuccessful(status)) {
            if (responseClass == null || body == null || body.isEmpty()) {
                return null;
            }
            return gson.fromJson(body, responseClass);
        }

        String message;
        switch (status) {
            case 400 -> message = "Bad request — please check your input.";
            case 401 -> message = "Unauthorized — please log in again.";
            case 403 -> message = "Forbidden — that name or slot is already taken.";
            case 404 -> message = "Not found.";
            case 500 -> message = "Server error — please try again later.";
            default -> message = "Unexpected error (" + status + ").";
        }
        throw new ResponseException(ResponseException.fromHttpStatusCode(status), message);
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
