package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece piece;

    QueenMovesCalculator(ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board = board;
        this.position = position;
        this.piece = piece;
    }

    public Collection<ChessMove> returnMoves() {
        var moves = new HashSet<ChessMove>();
        ChessGame.TeamColor team = piece.getTeamColor();
        // runs through the for loop 8 times, once for each direction
        for (int i = 0; i < 8; i++) {
            if (i == 0) {
                // up-left
                int row = position.getRow();
                int col = position.getColumn();
                while (true) {
                    row++;
                    col--;
                    if (row > 8 || col < 1) {
                        break;
                    }
                    // passed out-of-bounds check
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
                // up-right
                int row = position.getRow();
                int col = position.getColumn();
                while (true) {
                    row++;
                    col++;
                    if (row > 8 || col > 8) {
                        break;
                    }
                    // passed out-of-bounds check
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
                // down-right
                int row = position.getRow();
                int col = position.getColumn();
                while (true) {
                    row--;
                    col++;
                    if (row < 1 || col > 8) {
                        break;
                    }
                    // passed out-of-bounds check
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
            else if (i == 3) {
                // down-left
                int row = position.getRow();
                int col = position.getColumn();
                while (true) {
                    row--;
                    col--;
                    if (row < 1 || col < 1) {
                        break;
                    }
                    // passed out-of-bounds check
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
            if (i == 4) {
                // left
                int row = position.getRow();
                int col = position.getColumn();
                while (true) {
                    col--;
                    if (col < 1) {
                        break;
                    }
                    // passed out-of-bounds check
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
            else if (i == 5) {
                // up
                int row = position.getRow();
                int col = position.getColumn();
                while (true) {
                    row++;
                    if (row > 8) {
                        break;
                    }
                    // passed out-of-bounds check
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
            else if (i == 6) {
                // right
                int row = position.getRow();
                int col = position.getColumn();
                while (true) {
                    col++;
                    if (col > 8) {
                        break;
                    }
                    // passed out-of-bounds check
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
