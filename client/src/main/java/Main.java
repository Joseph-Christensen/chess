import exception.ResponseException;
import ui.*;

import static ui.EscapeSequences.*;

public class Main {
    public static void main(String[] args) throws ResponseException {
        // start the repl here
        System.out.println(SET_TEXT_COLOR_WHITE + "♕ Welcome to 240 Chess ♕");
        ChessClient client = new ChessClient("http://localhost:8080");
        client.repl();
    }
}