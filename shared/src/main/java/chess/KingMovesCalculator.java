package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator extends PieceMovesCalculator {

    public KingMovesCalculator(ChessBoard board, ChessPosition position, ChessPiece piece) {
        super(board, position, piece);
    }

    @Override
    public Collection<ChessMove> returnMoves() {
        int[][] directions = {
                {-1, 1}, {1, 1}, {1, -1}, {-1, -1},
                {1, 0}, {0, 1}, {-1, 0}, {0, -1}
        };
        return collectSingleStepMoves(directions);
    }
}