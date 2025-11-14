package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class ChessboardDisplay {

    private static final int SIZE = 8;
    private static final int SQUARE_WIDTH = 3;
    private static final int SQUARE_HEIGHT = 3;

    private static final String EMPTY = " ".repeat(SQUARE_WIDTH);

    private static final String RESET = "\u001b[0m";

    public static void drawHorizontalFrameWhite(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(EMPTY);
        for (char c = 'a'; c <= 'h'; c++) {
            out.print("  " + c + " ");
        }
        out.print(EMPTY);
        out.println(RESET);
    }

    public static void drawHorizontalFrameBlack(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(EMPTY);
        for (char c = 'h'; c >= 'a'; c--) {
            out.print("  " + c + " ");
        }
        out.print(EMPTY);
        out.println(RESET);
    }
}
