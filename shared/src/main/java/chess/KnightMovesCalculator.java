package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece piece;

    KnightMovesCalculator(ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board = board;
        this.position = position;
        this.piece = piece;
    }

    public ChessMove processMove(ChessPosition currPos, ChessGame.TeamColor team) {
        if (board.getPiece(currPos) == null) {
            // no piece
            return new ChessMove(position, currPos, null);
        }
        else {
            // piece detected
            if (board.getPiece(currPos).getTeamColor() != team) {
                // different team
                return new ChessMove(position, currPos, null);
            }
        }
        return null;
    }

    public Collection<ChessMove> returnMoves() {
        var moves = new HashSet<ChessMove>();
        ChessGame.TeamColor team = piece.getTeamColor();

        for (int i = 0; i < 8; i++) {
            // goes through 8 times for each direction
            int row = position.getRow();
            int col = position.getColumn();

            if (i == 0) {
                // left-up
                col--;
                col--;
                row++;
                if (col > 0 && row < 9) {
                    // continue
                    ChessPosition currPos = new ChessPosition(row, col);
                    if (processMove(currPos, team) != null) {
                        moves.add(processMove(currPos, team));
                    }
                }
            }
            else if (i == 1) {
                // up-left
                col--;
                row++;
                row++;
                if (col > 0 && row < 9) {
                    // continue
                    ChessPosition currPos = new ChessPosition(row, col);
                    if (processMove(currPos, team) != null) {
                        moves.add(processMove(currPos, team));
                    }
                }
            }
            else if (i == 2) {
                // up-right
                row++;
                row++;
                col++;
                if (row < 9 && col < 9) {
                    // continue
                    ChessPosition currPos = new ChessPosition(row, col);
                    if (processMove(currPos, team) != null) {
                        moves.add(processMove(currPos, team));
                    }
                }
            }
            else if (i == 3) {
                // right-up
                col++;
                col++;
                row++;
                if (col < 9 && row < 9) {
                    // continue
                    ChessPosition currPos = new ChessPosition(row, col);
                    if (processMove(currPos, team) != null) {
                        moves.add(processMove(currPos, team));
                    }
                }
            }
            else if (i == 4) {
                // right-down
                col++;
                col++;
                row--;
                if (col < 9 && row > 0) {
                    // continue
                    ChessPosition currPos = new ChessPosition(row, col);
                    if (processMove(currPos, team) != null) {
                        moves.add(processMove(currPos, team));
                    }
                }
            }
            else if (i == 5) {
                // down-right
                row--;
                row--;
                col++;
                if (col < 9 && row > 0) {
                    // continue
                    ChessPosition currPos = new ChessPosition(row, col);
                    if (processMove(currPos, team) != null) {
                        moves.add(processMove(currPos, team));
                    }
                }
            }
            else if (i == 6) {
                // down-left
                row--;
                row--;
                col--;
                if (row > 0 && col > 0) {
                    // continue
                    ChessPosition currPos = new ChessPosition(row, col);
                    if (processMove(currPos, team) != null) {
                        moves.add(processMove(currPos, team));
                    }
                }
            }
            else {
                // left-down
                col--;
                col--;
                row--;
                if (col > 0 && row > 0) {
                    // continue
                    ChessPosition currPos = new ChessPosition(row, col);
                    if (processMove(currPos, team) != null) {
                        moves.add(processMove(currPos, team));
                    }
                }
            }
        }
        return moves;
    }
}
