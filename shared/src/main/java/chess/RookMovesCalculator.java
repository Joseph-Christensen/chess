package chess;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final ChessPiece piece;

    RookMovesCalculator (ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        this.board = board;
        this.myPosition = myPosition;
        this.piece = piece;
    }

    public Collection<ChessMove> returnMoves() {
        var moves = new HashSet<ChessMove>();
        ChessGame.TeamColor pieceColor = piece.getTeamColor();
        for (int i = 0; i < 4; i++) {
            // runs though 4 times for each rook direction, left, up, right, down
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            if (i == 0) {
                // left
                while (true) {
                    col--;
                    if (col < 1) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
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
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
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
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
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
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
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
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
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
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
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
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
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
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
        }
        return moves;
    }
}
