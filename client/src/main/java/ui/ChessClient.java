package ui;

import exception.ResponseException;
import model.*;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {

    private State state = State.SIGNEDOUT;
    private final ServerFacade server;
    private String authToken = null;
    private String username = null;

    public ChessClient (String serverURL) {
        server = new ServerFacade(serverURL);
    }

    public void repl() {
        System.out.print("  " + help());

        Scanner scanner = new Scanner(System.in);
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
            String[] tokens = input.toLowerCase().trim().split("\\s+");
            String flag = tokens[0];
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
                case "leave" -> leave();
                default -> invalidCommand();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
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
                    SET_TEXT_COLOR_BLUE + "leave - " +
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

    private String failure(String action, ResponseException ex) {
        return String.format("%s failed: %s", action, ex.getMessage());
    }

    private String register(String[] params) {
        if (state != State.SIGNEDOUT) return invalidCommand();
        if (params.length != 3) return "Please enter a username, password, and email.";
        try {
            var user = new UserData(params[0], params[1], params[2]);
            AuthData auth = server.register(user);
            username = auth.username();
            authToken = auth.authToken();
            state = State.SIGNEDIN;
            return success("Register", "logged in as " + username + ".") + "\n  " + help();
        } catch (ResponseException ex) {
            return failure("Register", ex);
        }
    }

    private String login(String[] params) {
        if (state != State.SIGNEDOUT) return invalidCommand();
        if (params.length != 2) return "Please enter a username and password.";
        try {
            LoginInfo info = new LoginInfo(params[0], params[1]);
            AuthData auth = server.login(info);
            username = auth.username();
            authToken = auth.authToken();
            state = State.SIGNEDIN;
            return success("Login", "logged in as " + username + ".") + "\n  " + help();
        } catch (ResponseException ex) {
            return failure("Login", ex);
        }
    }

    private String create(String[] params) {
        if (state != State.SIGNEDIN) return invalidCommand();
        if (params.length != 1) return "Please enter a name for the game.";
        try {
            GameEntry game = new GameEntry(params[0]);
            server.createGame(game, authToken);
            return success("Create", "created game with name " + game.gameName());
        } catch (ResponseException ex) {
            return failure("Create", ex);
        }
    }

    private String list() {
        if (state != State.SIGNEDIN) return invalidCommand();
        try {
            server.listGames(authToken);
        } catch (ResponseException ex) {
            return failure("List", ex);
        }
        return "Called List";
    }

    private String join(String[] params) {
        if (state != State.SIGNEDIN) {
            return invalidCommand();
        }
        if (params.length != 2) {
            return "Please enter a game id and color to join.";
        }
        if (!params[0].matches("\\d+")) {
            return "Please enter a valid game id: \"" + params[0] + "\"";
        }
        String color;
        if (params[1].toLowerCase().matches("black")) {
            color = "BLACK";
        } else if (params[1].toLowerCase().matches("white")) {
            color = "WHITE";
        } else {
            return "Please enter a valid color to join: \"BLACK\" or \"WHITE\"";
        }
        state = State.INGAME;
        return "Called Join with id " + params[0] + " and color " + color + "\n  " + help();
    }

    private String observe(String[] params) {
        if (state != State.SIGNEDIN) {
            return invalidCommand();
        }
        if (params.length != 1) {
            return "Please enter a game id.";
        }
        if (!params[0].matches("\\d+")) {
            return "Please enter a valid game id: \"" + params[0] + "\"";
        }
        state = State.INGAME;
        return "Called Observe\n  " + help();
    }

    private String logout() {
        if (state != State.SIGNEDIN) return invalidCommand();
        try {
            server.logout(authToken);
            username = null;
            authToken = null;
            state = State.SIGNEDOUT;
            return success("Logout", "logged out.")+ "\n  " + help();
        } catch (ResponseException ex) {
            return failure("Logout", ex);
        }
    }

    private String leave() {
        if (state != State.INGAME) {
            return invalidCommand();
        }
        state = State.SIGNEDIN;
        return "Left Game\n  " + help();
    }
}
