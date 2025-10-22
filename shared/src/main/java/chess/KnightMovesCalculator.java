package chess;

import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {

    public KnightMovesCalculator(ChessBoard board, ChessPosition position, ChessPiece piece) {
        super(board, position, piece);
    }

    @Override
    public Collection<ChessMove> returnMoves() {
        int[][] directions = {
                {-2, 1}, {-1, 2}, {1, 2}, {2, 1},
                {2, -1}, {1, -2}, {-1, -2}, {-2, -1}
        };
        return collectSingleStepMoves(directions);
    }
}