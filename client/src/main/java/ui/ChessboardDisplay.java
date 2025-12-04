package ui;

import chess.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;
import static ui.EscapeSequences.*;

public class ChessboardDisplay {

    private static final int SIZE = 8;
    private static final int SQUARE_WIDTH = 3;

    private static final String EMPTY = " ".repeat(SQUARE_WIDTH);

    private static final String RESET = "\u001b[0m";

    private static boolean whiteBackground = true;

    public static void drawWhiteBoard(PrintStream out, ChessBoard board, Collection<ChessMove> validMoves) {
        ChessPosition start = null;
        List<ChessPosition> endPositions = new ArrayList<>();
        if (validMoves != null) {
            for (ChessMove move : validMoves) {
                start = move.getStartPosition();
                endPositions.add(move.getEndPosition());
            }
        }
        out.println();
        out.println();
        drawHorizontalFrameWhite(out);
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 8 ");
        for (int i = SIZE; i > 0; i--) {
            for (int j = 1; j < SIZE + 1; j++) {

                processPosition(i, j, board, validMoves, start, endPositions, out);

                if (j == 8 && i != 1) {
                    drawEdge(out, i, i - 1);
                } else {
                    whiteBackground = !whiteBackground;
                }
            }
        }
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 1 ");
        out.println(RESET);
        drawHorizontalFrameWhite(out);
        out.print(RESET);
        whiteBackground = true;
    }

    public static void drawBlackBoard(PrintStream out, ChessBoard board, Collection<ChessMove> validMoves) {
        ChessPosition start = null;
        List<ChessPosition> endPositions = new ArrayList<>();
        if (validMoves != null) {
            for (ChessMove move : validMoves) {
                start = move.getStartPosition();
                endPositions.add(move.getEndPosition());
            }
        }
        out.println();
        out.println();
        drawHorizontalFrameBlack(out);
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 1 ");
        for (int i = 1; i < SIZE + 1; i++) {
            for (int j = SIZE; j > 0; j--) {

                processPosition(i, j, board, validMoves, start, endPositions, out);

                if (j == 1 && i != 8) {
                    drawEdge(out, i, i + 1);
                } else {
                    whiteBackground = !whiteBackground;
                }
            }
        }
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" 8 ");
        out.println(RESET);
        drawHorizontalFrameBlack(out);
        out.print(RESET);
        whiteBackground = true;
    }

    private static boolean posIsEven(ChessPosition pos) {
        return (pos.getRow() + pos.getColumn()) % 2 == 0;
    }

    private static void drawEdge(PrintStream out, int row1, int row2) {
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" " + row1 + " ");
        out.println(RESET);
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        out.print(" " + row2 + " ");
    }

    private static String getString(ChessPiece piece) {
        String pieceRep = "";
        if (piece == null) {
            pieceRep += " ";
        } else if (PAWN.equals(piece.getPieceType())) {
            pieceRep += "P";
        } else if (ROOK.equals(piece.getPieceType())) {
            pieceRep += "R";
        } else if (KNIGHT.equals(piece.getPieceType())) {
            pieceRep += "N";
        } else if (BISHOP.equals(piece.getPieceType())) {
            pieceRep += "B";
        } else if (QUEEN.equals(piece.getPieceType())) {
            pieceRep += "Q";
        } else {
            pieceRep += "K";
        }
        return pieceRep;
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

    private static void processPosition(int i, int j, ChessBoard board, Collection<ChessMove> validMoves,
                                        ChessPosition start, List<ChessPosition> endPositions, PrintStream out) {
        ChessPosition pos = new ChessPosition(i, j);
        ChessPiece piece = board.getPiece(pos);


        if (piece != null) {
            if (piece.getTeamColor().equals(WHITE)) {
                out.print(SET_TEXT_COLOR_RED);
            } else {
                out.print(SET_TEXT_COLOR_BLUE);
            }
        }

        if (validMoves == null) {
            if (whiteBackground) {
                out.print(SET_BG_COLOR_WHITE);
            } else {
                out.print(SET_BG_COLOR_BLACK);
            }
        } else {
            if (pos.equals(start)) {
                out.print(SET_BG_COLOR_YELLOW);
            } else if (endPositions.contains(pos) && posIsEven(pos)) {
                out.print(SET_BG_COLOR_DARK_GREEN);
            } else if (endPositions.contains(pos)) {
                out.print(SET_BG_COLOR_GREEN);
            } else if (whiteBackground) {
                out.print(SET_BG_COLOR_WHITE);
            } else {
                out.print(SET_BG_COLOR_BLACK);
            }
        }

        // get the String being printed
        String pieceRep = getString(piece);

        out.print(" " + pieceRep + " ");
    }
}
