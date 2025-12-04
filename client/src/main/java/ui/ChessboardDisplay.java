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

    public static void drawWhiteBoardInitial(PrintStream out) {
        drawHorizontalFrameWhite(out);
        drawTopPiecesWhite(out);
        drawEmptyRows(out, true);
        drawBottomPiecesWhite(out);
        drawHorizontalFrameWhite(out);
        out.print(RESET);
        whiteBackground = true;
    }

    public static void drawBlackBoardInitial(PrintStream out) {
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
