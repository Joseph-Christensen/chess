package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator extends PieceMovesCalculator {

    public KnightMovesCalculator(ChessBoard board, ChessPosition position, ChessPiece piece) {
        super(board, position, piece);
    }

    @Override
    public Collection<ChessMove> returnMoves() {
        var moves = new HashSet<ChessMove>();
        int[][] directions = {{-2,1},{-1,2},{1,2},{2,1},{2,-1},{1,-2},{-1,-2},{-2,-1}};
        for (int[] direction : directions) {
            if (positionMoveCalc(direction[0], direction[1], board, position, piece.getTeamColor()) != null) {
                moves.add(positionMoveCalc(direction[0], direction[1], board, position, piece.getTeamColor()));
            }
        }
        return moves;
    }
}