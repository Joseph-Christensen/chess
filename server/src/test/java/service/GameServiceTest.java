package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Test
    void listGames() throws ChessException {
        DataAccess db = new MemoryDataAccess();
        UserService userService = new UserService(db);
        GameService gameService = new GameService(db);
        UserData user = new UserData("joe", "manysecrets", "j@j.com");
        var authData = userService.register(user);
        GameData game = new GameData(1, null, null, "MyGame", new ChessGame());
        gameService.createGame(new GameEntry(game.gameName()), authData.authToken());

        GameData gameTwo = new GameData(2, null, null, "SecondGame", new ChessGame());
        gameService.createGame(new GameEntry(gameTwo.gameName()), authData.authToken());

        var games = gameService.listGames(authData.authToken());

        assertTrue(games.contains(new GameRepresentation(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName())));
        assertTrue(games.contains(new GameRepresentation(gameTwo.gameID(), gameTwo.whiteUsername(), gameTwo.blackUsername(), gameTwo.gameName())));
    }

    @Test
    void listGamesWrongAuth() throws ChessException {
        DataAccess db = new MemoryDataAccess();
        UserService userService = new UserService(db);
        GameService gameService = new GameService(db);
        UserData user = new UserData("joe", "manysecrets", "j@j.com");
        var authData = userService.register(user);
        GameData game = new GameData(1, null, null, "MyGame", new ChessGame());
        gameService.createGame(new GameEntry(game.gameName()), authData.authToken());

        ChessException ex = assertThrows(
                ChessException.class,
                () -> gameService.listGames("MyAuthToken")
        );

        assertEquals(401, ex.getCode());
        assertEquals("unauthorized", ex.getMessage());
    }

    @Test
    void createGame() {
    }

    @Test
    void joinGame() {
    }
}