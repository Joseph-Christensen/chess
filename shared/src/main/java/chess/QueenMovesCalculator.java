package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece piece;

    QueenMovesCalculator (ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board = board;
        this.position = position;
        this.piece = piece;
    }

    public Collection<ChessMove> returnMoves() {
        // going to run through bishop logic and then rook logic
        var moves = new HashSet<ChessMove>();
        ChessGame.TeamColor pieceColor = piece.getTeamColor();
        // runs through bishop logic
        for (int i = 0; i < 4; i++) {
            // runs though 4 times for each diagonal queen direction, up-left, up-right, down-right, down-left
            int row = position.getRow();
            int col = position.getColumn();
            if (i == 0) {
                // up-left
                while (true) {
                    row++;
                    if (row > 8) {
                        break;
                    }
                    col--;
                    if (col < 1) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor == currPiece.getTeamColor()) {
                            // same team
                            break;
                        }
                        else {
                            // other team
                            moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
            else if (i == 1) {
                // up-right
                while (true) {
                    row++;
                    if (row > 8) {
                        break;
                    }
                    col++;
                    if (col > 8) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor == currPiece.getTeamColor()) {
                            // same team
                            break;
                        }
                        else {
                            // other team
                            moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
            else if (i == 2) {
                // down-right
                while (true) {
                    row--;
                    if (row < 1) {
                        break;
                    }
                    col++;
                    if (col > 8) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor == currPiece.getTeamColor()) {
                            // same team
                            break;
                        }
                        else {
                            // other team
                            moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
            else {
                // down-left
                while (true) {
                    row--;
                    if (row < 1) {
                        break;
                    }
                    col--;
                    if (col < 1) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor == currPiece.getTeamColor()) {
                            // same team
                            break;
                        }
                        else {
                            // other team
                            moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
        }
        // runs through the rook logic
        for (int i = 0; i < 4; i++) {
            // runs though 4 times for each straight queen direction, left, up, right, down
            int row = position.getRow();
            int col = position.getColumn();
            if (i == 0) {
                // left
                while (true) {
                    col--;
                    if (col < 1) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor == currPiece.getTeamColor()) {
                            // same team
                            break;
                        }
                        else {
                            // other team
                            moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
            else if (i == 1) {
                // up
                while (true) {
                    row++;
                    if (row > 8) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor == currPiece.getTeamColor()) {
                            // same team
                            break;
                        }
                        else {
                            // other team
                            moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
            else if (i == 2) {
                // right
                while (true) {
                    col++;
                    if (col > 8) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor == currPiece.getTeamColor()) {
                            // same team
                            break;
                        }
                        else {
                            // other team
                            moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
            else {
                // down
                while (true) {
                    row--;
                    if (row < 1) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor == currPiece.getTeamColor()) {
                            // same team
                            break;
                        }
                        else {
                            // other team
                            moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
        }
        return moves;
    }
}
