package ui;

import java.io.PrintStream;
import static ui.EscapeSequences.*;

public class ChessboardDisplay {

    private static final int SIZE = 8;
    private static final int SQUARE_WIDTH = 3;

    private static final String EMPTY = " ".repeat(SQUARE_WIDTH);

    private static final String RESET = "\u001b[0m";

    private static boolean whiteBackground = true;

    public static void drawWhiteBoard(PrintStream out) {
        drawHorizontalFrameWhite(out);
        drawTopPiecesWhite(out);
        drawEmptyRows(out, true);
        drawBottomPiecesWhite(out);
        drawHorizontalFrameWhite(out);
        out.print(RESET);
        whiteBackground = true;
    }

    public static void drawBlackBoard(PrintStream out) {
        drawHorizontalFrameBlack(out);
        drawTopPiecesBlack(out);
        drawEmptyRows(out, false);
        drawBottomPiecesBlack(out);
        drawHorizontalFrameBlack(out);
        out.print(RESET);
        whiteBackground = true;
    }

    private static void drawHorizontalFrameWhite(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(EMPTY);
        for (char c = 'a'; c <= 'h'; c++) {
            out.print(" " + c + " ");
        }
        out.print(EMPTY);
        out.println(RESET);
    }

    private static void drawHorizontalFrameBlack(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(EMPTY);
        for (char c = 'h'; c >= 'a'; c--) {
            out.print(" " + c + " ");
        }
        out.print(EMPTY);
        out.println(RESET);
    }

    private static void drawTopPiecesWhite(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 8 ");
        drawRow(out, false, true, false, false);
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 8 ");
        out.println(RESET);
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 7 ");
        whiteBackground = false;
        drawRow(out, false, true, true, false);
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 7 ");
        out.println(RESET);
    }

    private static void drawTopPiecesBlack(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 1 ");
        drawRow(out, true, false, false, false);
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 1 ");
        out.println(RESET);
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 2 ");
        whiteBackground = false;
        drawRow(out, true, false, true, false);
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 2 ");
        out.println(RESET);
    }

    private static void drawEmptyRows(PrintStream out, boolean playerWhite) {
        if (playerWhite) {
            for (int i = 6; i >= 3; i--) {
                out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
                out.print(" " + i + " ");
                whiteBackground = i % 2 == 0;
                drawRow(out, true, false, false, true);
                out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
                out.print(" " + i + " ");
                out.println(RESET);
            }
        } else {
            for (int i = 3; i <= 6; i++) {
                out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
                out.print(" " + i + " ");
                whiteBackground = i % 2 == 1;
                drawRow(out, true, false, false, true);
                out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
                out.print(" " + i + " ");
                out.println(RESET);
            }
        }
        whiteBackground = true;
    }

    private static void drawBottomPiecesWhite(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 2 ");
        drawRow(out, true, true, true, false);
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 2 ");
        out.println(RESET);
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 1 ");
        whiteBackground = false;
        drawRow(out, true, true, false, false);
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 1 ");
        out.println(RESET);
    }

    private static void drawBottomPiecesBlack(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 7 ");
        drawRow(out, false, false, true, false);
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 7 ");
        out.println(RESET);
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 8 ");
        whiteBackground = false;
        drawRow(out, false, false, false, false);
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 8 ");
        out.println(RESET);
    }

    private static void drawRow(PrintStream out, boolean piecesWhite, boolean playerWhite, boolean isPawn, boolean isEmpty) {
        if (piecesWhite) {
            out.print(SET_TEXT_COLOR_RED);
        } else {
            out.print(SET_TEXT_COLOR_BLUE);
        }
        String[] order;
        if (isEmpty) {
            order = new String[] {" ", " ", " ", " ", " ", " ", " ", " "};
        } else if (isPawn) {
            order = new String[] {"P", "P", "P", "P", "P", "P", "P", "P"};
        } else if (playerWhite) {
            order = new String[] {"R", "N", "B", "Q", "K", "B", "N", "R"};
        } else {
            order = new String[] {"R", "N", "B", "K", "Q", "B", "N", "R"};
        }

        for (int i = 0; i < SIZE; i++) {
            if (whiteBackground) {
                out.print(SET_BG_COLOR_WHITE);
                whiteBackground = false;
            } else {
                out.print(SET_BG_COLOR_BLACK);
                whiteBackground = true;
            }
            out.print(" " + order[i] + " ");
        }
        out.print(RESET);
    }
}
