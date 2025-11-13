package ui;

import exception.ResponseException;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {

    private State state = State.SIGNEDOUT;

    public ChessClient () {}

    public void repl() {
        System.out.print(help());

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
                case "register" -> "Called Register";
                case "login" -> "Called Login";
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
            return  SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL> - " +
                    SET_TEXT_COLOR_WHITE + "creates a new account\n" +
                    SET_TEXT_COLOR_BLUE + "  login <USERNAME> <PASSWORD> - " +
                    SET_TEXT_COLOR_WHITE + "logs you into an existing account\n" +
                    SET_TEXT_COLOR_BLUE + "  quit - " +
                    SET_TEXT_COLOR_WHITE + "exits the chess client\n" +
                    SET_TEXT_COLOR_BLUE + "  help - " +
                    SET_TEXT_COLOR_WHITE + "lists possible commands";
        }
        return """
                - list
                - adopt <pet id>
                - rescue <name> <CAT|DOG|FROG|FISH>
                - adoptAll
                - signOut
                - quit
                """;
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
}
