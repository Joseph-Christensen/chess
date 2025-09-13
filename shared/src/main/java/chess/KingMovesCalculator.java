package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final ChessPiece piece;

    KingMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        this.board = board;
        this.myPosition = myPosition;
        this.piece = piece;
    }

    public Collection<ChessMove> returnMoves() {
        var moves = new HashSet<ChessMove>();
        ChessGame.TeamColor pieceColor = piece.getTeamColor();
        for (int i = 0; i < 8; i++) {
            // runs though 8 times for each rook direction, left, up-left, up, up-right, right, down-right, down, down-left
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            if (i == 0) {
                // left
                col--;
                if (col > 0) {
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor != currPiece.getTeamColor()) {
                            // different team
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
            else if (i == 1) {
                // up-left
                row++;
                col--;
                if ((col > 0) && (row < 9)) {
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor != currPiece.getTeamColor()) {
                            // different team
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
            else if (i == 2) {
                // up
                row++;
                if (row < 9) {
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor != currPiece.getTeamColor()) {
                            // different team
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
            else if (i == 3) {
                // up-right
                row++;
                col++;
                if ((col < 9) && (row < 9)) {
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor != currPiece.getTeamColor()) {
                            // different team
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
            else if (i == 4) {
                // right
                col++;
                if (col < 9) {
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor != currPiece.getTeamColor()) {
                            // different team
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
            else if (i == 5) {
                // down-right
                row--;
                col++;
                if ((col < 9) && (row > 0)) {
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor != currPiece.getTeamColor()) {
                            // different team
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
            else if (i == 6) {
                // down
                row--;
                if (row > 0) {
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor != currPiece.getTeamColor()) {
                            // different team
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
            else {
                // down-left
                row--;
                col--;
                if ((col > 0) && (row > 0)) {
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor != currPiece.getTeamColor()) {
                            // different team
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
        }
        return moves;
    }
}
