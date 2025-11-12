package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {

    private static State state = State.SIGNEDOUT;

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        System.out.println(ERASE_SCREEN + " Welcome to the pet store. Sign in to start.");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            if (state == State.SIGNEDOUT) {
                state = State.SIGNEDIN;
            } else {
                state = State.SIGNEDOUT;
            }

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public static String eval(String line) {
        if (line.equalsIgnoreCase("help")) {
            System.out.print(help());
        }
        return line;
    }

    private static void printPrompt() {
        System.out.print("\n" + RESET_TEXT_BOLD_FAINT + ">>> " + SET_TEXT_COLOR_GREEN );
    }

    public static String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - signIn <yourname>
                    - quit
                    """;
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

}
