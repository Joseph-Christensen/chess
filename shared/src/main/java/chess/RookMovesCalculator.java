package chess;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece piece;

    RookMovesCalculator(ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board = board;
        this.position = position;
        this.piece = piece;
    }

    public Collection<ChessMove> returnMoves() {
        var moves = new HashSet<ChessMove>();
        ChessGame.TeamColor team = piece.getTeamColor();
        // runs through the for loop 4 times, once for each direction
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                // left
                int row = position.getRow();
                int col = position.getColumn();
                while (true) {
                    col--;
                    if (col < 1) {
                        break;
                    }
                    // passed out-of-bounds check

                    // check to see if there is a piece there

                    ChessPosition currPos = new ChessPosition(row, col);
                    if (board.getPiece(currPos) == null) {
                        // keep going
                        moves.add(new ChessMove(position, currPos, null));
                    }
                    else {
                        // piece detected
                        if (board.getPiece(currPos).getTeamColor() != team) {
                            // different team
                            moves.add(new ChessMove(position, currPos, null));
                        }
                        break;
                    }
                }
            }
            else if (i == 1) {
                // up
                int row = position.getRow();
                int col = position.getColumn();
                while (true) {
                    row++;
                    if (row > 8) {
                        break;
                    }
                    // passed out-of-bounds check

                    // check to see if there is a piece there

                    ChessPosition currPos = new ChessPosition(row, col);
                    if (board.getPiece(currPos) == null) {
                        // keep going
                        moves.add(new ChessMove(position, currPos, null));
                    }
                    else {
                        // piece detected
                        if (board.getPiece(currPos).getTeamColor() != team) {
                            // different team
                            moves.add(new ChessMove(position, currPos, null));
                        }
                        break;
                    }
                }
            }
            else if (i == 2) {
                // right
                int row = position.getRow();
                int col = position.getColumn();
                while (true) {
                    col++;
                    if (col > 8) {
                        break;
                    }
                    // passed out-of-bounds check

                    // check to see if there is a piece there

                    ChessPosition currPos = new ChessPosition(row, col);
                    if (board.getPiece(currPos) == null) {
                        // keep going
                        moves.add(new ChessMove(position, currPos, null));
                    }
                    else {
                        // piece detected
                        if (board.getPiece(currPos).getTeamColor() != team) {
                            // different team
                            moves.add(new ChessMove(position, currPos, null));
                        }
                        break;
                    }
                }
            }
            else {
                // down
                int row = position.getRow();
                int col = position.getColumn();
                while (true) {
                    row--;
                    if (row < 1) {
                        break;
                    }
                    // passed out-of-bounds check

                    // check to see if there is a piece there

                    ChessPosition currPos = new ChessPosition(row, col);
                    if (board.getPiece(currPos) == null) {
                        // keep going
                        moves.add(new ChessMove(position, currPos, null));
                    }
                    else {
                        // piece detected
                        if (board.getPiece(currPos).getTeamColor() != team) {
                            // different team
                            moves.add(new ChessMove(position, currPos, null));
                        }
                        break;
                    }
                }
            }
        }
        return moves;
    }
}
