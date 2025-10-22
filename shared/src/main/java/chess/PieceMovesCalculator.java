package chess;

import java.util.Collection;
import java.util.HashSet;

public class PieceMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece piece;

    PieceMovesCalculator(ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board = board;
        this.position = position;
        this.piece = piece;
    }

    public Collection<ChessMove> returnMoves() {
        return new HashSet<ChessMove>();
    }

    private Collection<ChessMove> directionalMoveCalc(int rowDir, int colDir, ChessBoard board, ChessPosition startPos, ChessGame.TeamColor team) {
        var moves = new HashSet<ChessMove>();
        int row = startPos.getRow() + rowDir;
        int col = startPos.getColumn() + colDir;

        while (true) {
            if (row < 1 || row > 8) {
                break;
            }
            if (col < 1 || col > 8) {
                break;
            }
            ChessPosition currPos = new ChessPosition(row, col);

            if (board.getPiece(currPos) == null) {
                // keep going
                moves.add(new ChessMove(startPos, currPos, null));
            } else {
                if (board.getPiece(currPos).getTeamColor() != team) {
                    // different team
                    moves.add(new ChessMove(startPos, currPos, null));
                }
                break;
            }
            row += rowDir;
            col += colDir;
        }
        return moves;
    }
}
