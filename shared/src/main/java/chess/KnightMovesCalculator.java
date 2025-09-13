package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece piece;

    KnightMovesCalculator (ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board = board;
        this.position = position;
        this.piece = piece;
    }

    public Collection<ChessMove> returnMoves() {
        var moves = new HashSet<ChessMove>();
        ChessGame.TeamColor pieceColor = piece.getTeamColor();
        for (int i = 0; i < 8; i++) {
            // runs though 8 times for each rook direction, left-up, up-left, up-right, right-up, right-down, down-right, down-left, left-down
            int row = position.getRow();
            int col = position.getColumn();
            if (i == 0) {
                // left-up
                col--;
                col--;
                row++;
                if (col > 0 && row < 9) {
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor != currPiece.getTeamColor()) {
                            // different team
                            moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
            else if (i == 1) {
                // up-left
                col--;
                row++;
                row++;
                if ((col > 0) && (row < 9)) {
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor != currPiece.getTeamColor()) {
                            // different team
                            moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
            else if (i == 2) {
                // up-right
                col++;
                row++;
                row++;
                if (col < 9 && row < 9) {
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor != currPiece.getTeamColor()) {
                            // different team
                            moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
            else if (i == 3) {
                // right-up
                col++;
                col++;
                row++;
                if (col < 9 && row < 9) {
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor != currPiece.getTeamColor()) {
                            // different team
                            moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
            else if (i == 4) {
                // right-down
                col++;
                col++;
                row--;
                if (col < 9 && row > 0) {
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor != currPiece.getTeamColor()) {
                            // different team
                            moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
            else if (i == 5) {
                // down-right
                col++;
                row--;
                row--;
                if ((col < 9) && (row > 0)) {
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor != currPiece.getTeamColor()) {
                            // different team
                            moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
            else if (i == 6) {
                // down-left
                row--;
                row--;
                col--;
                if (col> 0 && row > 0) {
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor != currPiece.getTeamColor()) {
                            // different team
                            moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
            else {
                // left-down
                col--;
                col--;
                row--;
                if (col > 0 && row > 0) {
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor != currPiece.getTeamColor()) {
                            // different team
                            moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
        }
        return moves;
    }
}
