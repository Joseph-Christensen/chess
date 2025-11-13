package ui;

import exception.ResponseException;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {

    private State state = State.SIGNEDOUT;
    private final ServerFacade server;

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
                System.out.print(SET_TEXT_COLOR_BLUE + "  " + result);
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
                default -> "Please enter a valid command.\n  Type 'help' to view possible commands.";
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

    private String register(String[] params) {
        if (params.length != 3) {
            return "Please enter a username, password, and email.";
        }
        state = State.SIGNEDIN;
        return "Logged In\n  " + help();
    }

    private String login(String[] params) {
        if (params.length != 2) {
            return "Please enter a username and password.";
        }
        state = State.SIGNEDIN;
        return "Logged In\n  " + help();
    }

    private String create(String[] params) {
        if (params.length != 1) {
            return "Please enter a name for the game.";
        }
        return "Called Create";
    }

    private String list() {
        return "Called List";
    }

    private String join(String[] params) {
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
        return "Called Join with id" + params[0] + " and color " + color + "\n  " + help();
    }

    private String observe(String[] params) {
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
        state = State.SIGNEDOUT;
        return "Logged Out\n  " + help();
    }

    private String leave() {
        state = State.SIGNEDIN;
        return "Left Game\n  " + help();
    }
}
