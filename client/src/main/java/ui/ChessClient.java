package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import server.NotificationHandler;
import server.ServerFacade;
import server.WebSocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static chess.ChessGame.TeamColor.*;
import static exception.ResponseException.Code.*;
import static ui.EscapeSequences.*;

public class ChessClient implements NotificationHandler {

    private State state = State.SIGNEDOUT;
    private final ServerFacade server;
    private final WebSocketFacade ws;
    private String authToken = null;
    private String username = null;
    private ChessGame.TeamColor team = null;
    private ChessGame currentGame = new ChessGame();
    private int currentGameID = -1;
    private final Scanner scanner = new Scanner(System.in);
    private final Gson gson = new Gson();

    public ChessClient (String serverURL) throws ResponseException {
        server = new ServerFacade(serverURL);
        ws = new WebSocketFacade(serverURL, this);
    }

    public void repl() {
        System.out.print("  " + help());

        String line = "";
        while (!line.equals("quit")) {
            printPrompt();
            line = scanner.nextLine();

            try {
                String result = eval(line);
                System.out.print(SET_TEXT_COLOR_MAGENTA + "  " + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    private String eval(String input) {
        try {
            String[] tokens = input.trim().split("\\s+");
            String flag = tokens[0].toLowerCase();
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (flag) {
                case "help" -> help();
                case "quit" -> "Exiting Client";
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                case "move" -> makeMove(params);
                case "highlight" -> highlight(params);
                case "redraw" -> redraw(currentGame);
                case "resign" -> resign();
                case "leave" -> leave();
                default -> invalidCommand();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public void notify(ServerMessage notification) {
        System.out.println(SET_TEXT_COLOR_MAGENTA + notification.getMessage());
        printPrompt();
    }

    public void error(ErrorMessage errorMessage) {
        System.out.println(SET_TEXT_COLOR_MAGENTA + errorMessage.getErrorMessage());
        printPrompt();
    }

    public void displayGame(LoadGameMessage loadGameMessage) {
        ChessGame game = gson.fromJson(loadGameMessage.getGame(), ChessGame.class);
        currentGame = game;
        redraw(game);
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_WHITE + printStatus() + " >>> " + SET_TEXT_COLOR_GREEN );
    }

    private String help() {
        if (state == State.SIGNEDOUT) {
            return
                    SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL> - " +
                    SET_TEXT_COLOR_WHITE + "creates a new account\n" +
                    SET_TEXT_COLOR_BLUE + "  login <USERNAME> <PASSWORD> - " +
                    SET_TEXT_COLOR_WHITE + "logs you into an existing account\n" +
                    SET_TEXT_COLOR_BLUE + "  quit - " +
                    SET_TEXT_COLOR_WHITE + "exits the chess client\n" +
                    SET_TEXT_COLOR_BLUE + "  help - " +
                    SET_TEXT_COLOR_WHITE + "lists possible commands";
        }
        else if (state == State.SIGNEDIN) {
            return SET_TEXT_COLOR_BLUE + "create <GAMENAME> - " +
                    SET_TEXT_COLOR_WHITE + "creates a new game\n" +
                    SET_TEXT_COLOR_BLUE + "  list - " +
                    SET_TEXT_COLOR_WHITE + "lists all current games\n" +
                    SET_TEXT_COLOR_BLUE + "  join <ID> <WHITE|BLACK> - " +
                    SET_TEXT_COLOR_WHITE + "joins a current game\n" +
                    SET_TEXT_COLOR_BLUE + "  observe <ID> - " +
                    SET_TEXT_COLOR_WHITE + "observes a current game\n" +
                    SET_TEXT_COLOR_BLUE + "  logout - " +
                    SET_TEXT_COLOR_WHITE + "logs you out of your account\n" +
                    SET_TEXT_COLOR_BLUE + "  quit - " +
                    SET_TEXT_COLOR_WHITE + "exits the chess client\n" +
                    SET_TEXT_COLOR_BLUE + "  help - " +
                    SET_TEXT_COLOR_WHITE + "lists possible commands";
        }
        else {
            return
                    SET_TEXT_COLOR_BLUE + "move <STARTSPACE> <ENDSPACE> - " +
                    SET_TEXT_COLOR_WHITE + "makes a move\n" +
                    SET_TEXT_COLOR_BLUE + "  highlight <STARTSPACE> - " +
                    SET_TEXT_COLOR_WHITE + "highlights legal moves for a given starting space\n" +
                    SET_TEXT_COLOR_BLUE + "  redraw - " +
                    SET_TEXT_COLOR_WHITE + "redraws the board\n" +
                    SET_TEXT_COLOR_BLUE + "  resign - " +
                    SET_TEXT_COLOR_WHITE + "concedes the game\n" +
                    SET_TEXT_COLOR_BLUE + "  leave - " +
                    SET_TEXT_COLOR_WHITE + "leaves the game\n" +
                    SET_TEXT_COLOR_BLUE + "  quit - " +
                    SET_TEXT_COLOR_WHITE + "exits the chess client\n" +
                    SET_TEXT_COLOR_BLUE + "  help - " +
                    SET_TEXT_COLOR_WHITE + "lists possible commands";
        }

    }

    private String printStatus() {
        if (state == State.SIGNEDOUT) {
            return "[LOGGED_OUT]";
        } else if (state == State.SIGNEDIN) {
            return "[LOGGED_IN]";
        } else {
            return "[IN_GAME]";
        }
    }

    private String invalidCommand() {
        return "Please enter a valid command.\n  Type 'help' to view possible commands.";
    }

    private String success(String action, String details) {
        return String.format("%s successful: %s", action, details);
    }

    private String failure(String action, String message) {
        return String.format("%s failed: %s", action, message);
    }

    private String register(String[] params) {
        if (state != State.SIGNEDOUT) {return invalidCommand();}
        if (params.length != 3) {return "Please enter a username, password, and email.";}
        try {
            var user = new UserData(params[0], params[1], params[2]);
            AuthData auth = server.register(user);
            username = auth.username();
            authToken = auth.authToken();
            state = State.SIGNEDIN;
            return success("Register", "logged in as " + username + ".") + "\n  " + help();
        } catch (ResponseException ex) {
            String message = (ex.getCode() == ResponseException.fromHttpStatusCode(403)) ?
                    "Username already taken." : ex.getMessage();
            return failure("Register", message);
        }
    }

    private String login(String[] params) {
        if (state != State.SIGNEDOUT) {return invalidCommand();}
        if (params.length != 2) {return "Please enter a username and password.";}
        try {
            LoginInfo info = new LoginInfo(params[0], params[1]);
            AuthData auth = server.login(info);
            username = auth.username();
            authToken = auth.authToken();
            state = State.SIGNEDIN;
            return success("Login", "logged in as " + username + ".") + "\n  " + help();
        } catch (ResponseException ex) {
            String message = (ex.getCode() == ResponseException.fromHttpStatusCode(401)) ?
                    "Invalid username or password." : ex.getMessage();
            return failure("Login", message);
        }
    }

    private String create(String[] params) {
        if (state != State.SIGNEDIN) {return invalidCommand();}
        if (params.length != 1) {return "Please enter a name for the game.";}
        try {
            GameEntry game = new GameEntry(params[0]);
            server.createGame(game, authToken);
            return success("Create", "created \"" + game.gameName() + "\"");
        } catch (ResponseException ex) {
            return failure("Create", ex.getMessage());
        }
    }

    private String list() {
        if (state != State.SIGNEDIN) {return invalidCommand();}
        try {
            HashSet<GameRepresentation> gamesSet = server.listGames(authToken);
            if (gamesSet == null || gamesSet.isEmpty()) {
                return "No games found.";
            }

            List<GameRepresentation> games = new ArrayList<>(gamesSet);

            var sb = new StringBuilder();
            for (int i = 0; i < games.size(); i++) {
                GameRepresentation game = games.get(i);
                int displayID = i + 1;
                sb.append(String.format("\n  [%d] %s (White: %s | Black: %s )",
                        displayID,
                        game.gameName(),
                        game.whiteUsername() != null ? game.whiteUsername() : "—",
                        game.blackUsername() != null ? game.blackUsername() : "—")
                );
            }
            return success("List", sb.toString());
        } catch (ResponseException ex) {
            return failure("List", ex.getMessage());
        }
    }

    private String join(String[] params) {
        if (state != State.SIGNEDIN) {return invalidCommand();}
        if (params.length != 2) {return "Please enter a game id and color to join.";}
        if (!params[0].matches("\\d+")) {return "Please enter a number for your game id.";}
        String color;
        if (params[1].toLowerCase().matches("black")) {
            color = "BLACK";
            team = BLACK;
        } else if (params[1].toLowerCase().matches("white")) {
            color = "WHITE";
            team = WHITE;
        } else {
            return "Please enter a valid color to join: \"BLACK\" or \"WHITE\"";
        }
        int gameID = Integer.parseInt(params[0]);
        try {
            HashSet<GameRepresentation> gamesSet = server.listGames(authToken);
            JoinRequest req = getJoinRequest(gamesSet, gameID, color);
            currentGameID = req.gameID();
            server.joinGame(req, authToken);
            ws.join(req.gameID(), authToken);
            state = State.INGAME;
            return success("Join", "joined game " + gameID + " as " + req.playerColor() + "\n  " + help());
        } catch (ResponseException ex) {
            String message;
            if (ex.getCode() == ResponseException.fromHttpStatusCode(403)) {
                message = color + " in game " + gameID + " is already taken.";
            } else if (ex.getCode() == ResponseException.fromHttpStatusCode(400)) {
                message = "Game id \"" + gameID + "\" doesn't exist.";
            } else {
                message = ex.getMessage();
            }
            return failure("Join", message);
        } catch (IOException ex) {
            return failure("Join", ex.getMessage());
        }
    }

    private String observe(String[] params) {
        if (state != State.SIGNEDIN) {return invalidCommand();}
        if (params.length != 1) {return "Please enter a game id.";}
        if (!params[0].matches("\\d+")) {return "Please enter a number for your game id.";}
        int gameID = Integer.parseInt(params[0]);
        try {
            HashSet<GameRepresentation> gamesSet = server.listGames(authToken);
            JoinRequest req = getJoinRequest(gamesSet, gameID, null);
            currentGameID = req.gameID();
            server.observeGame(gameID, authToken);
            ws.join(req.gameID(), authToken);
            state = State.INGAME;
            return success("Observe", "observing game " + gameID + "\n  " + help());
        } catch (ResponseException ex) {
            String message = (ex.getCode() == ResponseException.fromHttpStatusCode(400)) ?
                    "Game id \"" + gameID + "\" doesn't exist." : ex.getMessage();
            return failure("Observe", message);
        } catch (IOException ex) {
            return failure("Observe", ex.getMessage());
        }
    }

    private String logout() {
        if (state != State.SIGNEDIN) {return invalidCommand();}
        try {
            server.logout(authToken);
            username = null;
            authToken = null;
            state = State.SIGNEDOUT;
            return success("Logout", "logged out.")+ "\n  " + help();
        } catch (ResponseException ex) {
            return failure("Logout", ex.getMessage());
        }
    }

    private String makeMove(String[] params) {
        if (state != State.INGAME) {return invalidCommand();}
        if (params.length != 2) {return "Please enter a start and end board space.";}
        // something checking valid move syntax
        try {
            if (currentGame.getTeamTurn() != team) {
                return "It isn't your turn.";
            }
            String startPos = params[0].toLowerCase();
            String endPos = params[1].toLowerCase();

            ChessPosition start = translatePosition(startPos);
            ChessPosition end = translatePosition(endPos);

            ChessPiece piece = currentGame.getBoard().getPiece(start);
            if (piece == null) {
                return "There is no piece on " + startPos + ".";
            }

            ChessGame.TeamColor pieceColor = piece.getTeamColor();

            if (pieceColor != team) {
                return "That is an enemy piece.";
            }

            ChessMove move;

            boolean isPawn = piece.getPieceType() == ChessPiece.PieceType.PAWN;
            boolean reachesEnd = (pieceColor == ChessGame.TeamColor.WHITE && end.getRow() == 8) ||
                            (pieceColor == ChessGame.TeamColor.BLACK && end.getRow() == 1);

            if (isPawn && reachesEnd) {

                System.out.println(SET_TEXT_COLOR_MAGENTA + "\n  What piece do you want to promote to? (Q|R|B|N): " + SET_TEXT_COLOR_GREEN);
                String choice = scanner.nextLine().trim().toUpperCase();

                ChessPiece.PieceType promotionPiece = null;

                while (promotionPiece == null) {
                    switch (choice) {
                        case "Q" -> promotionPiece = ChessPiece.PieceType.QUEEN;
                        case "R" -> promotionPiece = ChessPiece.PieceType.ROOK;
                        case "B" -> promotionPiece = ChessPiece.PieceType.BISHOP;
                        case "N" -> promotionPiece = ChessPiece.PieceType.KNIGHT;
                        default -> {
                            System.out.println(SET_TEXT_COLOR_MAGENTA + "\n  Invalid choice. Enter (Q|R|B|N): " + SET_TEXT_COLOR_GREEN);
                            choice = scanner.nextLine().trim().toUpperCase();
                        }
                    }
                }

                move = new ChessMove(start, end, promotionPiece);

            } else {
                // Normal move
                move = new ChessMove(start, end, null);
            }

            ws.makeMove(currentGameID, authToken, move);

            return success("Move", "Successfully moved from " + startPos + " to " + endPos + ".");
        } catch (ResponseException | IOException ex) {
            return failure("Move", ex.getMessage());
        }
    }

    private String highlight(String[] params) {
        if (state != State.INGAME) {return invalidCommand();}
        if (params.length != 1) {return "Please enter a board space.";}
        // something checking valid move syntax
        return params[0];
    }

    private String redraw(ChessGame game) {
        if (state != State.INGAME) {return invalidCommand();}
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        if (team.equals(BLACK)) {
            ChessboardDisplay.drawBlackBoard(out, game.getBoard());
        } else {
            ChessboardDisplay.drawWhiteBoard(out, game.getBoard());
        }
        return "Redraw Successful!";
    }

    private String resign() {
        if (state != State.INGAME) { return invalidCommand(); }
        try {
            ws.resign(currentGameID, authToken);
            return success("Resign", "You resigned.");
        } catch (IOException ex) {
            return failure("Resign", ex.getMessage());
        }
    }

    private String leave() {
        if (state != State.INGAME) {return invalidCommand();}
        try {
            ws.leave(currentGameID, authToken);
            state = State.SIGNEDIN;
            team = null;
            currentGame = new ChessGame();
            currentGameID = -1;
            return success("Leave", "Left the Game" + help());

        } catch (IOException ex) {
            return failure("Leave", ex.getMessage());
        }
    }

    private ChessPosition translatePosition(String pos) throws ResponseException {
        String errorMessage = "Chess positions must be in the format <a-h><1-8>.";
        if (pos.length() != 2) {
            throw new ResponseException(ResponseException.Code.BadRequest, errorMessage);
        }

        char colChar = pos.charAt(0);
        char rowChar = pos.charAt(1);

        // check a-h
        if (colChar < 'a' || colChar > 'h') {
            if (colChar < 'A' || colChar > 'H') {
                throw new ResponseException(ResponseException.Code.BadRequest, errorMessage);
            }
        }

        // check 1-8
        if (rowChar < '1' || rowChar > '8') {
            throw new ResponseException(ResponseException.Code.BadRequest, errorMessage);
        }

        colChar = Character.toLowerCase(colChar);

        int col = colChar - 'a' + 1;
        int row = rowChar - '1' + 1;

        return new ChessPosition(row, col);
    }

    private JoinRequest getJoinRequest(HashSet<GameRepresentation> gamesSet, int inputID, String color) throws ResponseException {
        if (gamesSet == null || gamesSet.isEmpty()) {
            throw new ResponseException(BadRequest, "No games available");
        }

        List<GameRepresentation> games = new ArrayList<>(gamesSet);

        if (inputID < 1 || inputID > games.size()) {
            throw new ResponseException(BadRequest, "Invalid game ID");
        }

        int gameID = games.get(inputID - 1).gameID();

        return new JoinRequest(color, gameID);
    }
}
