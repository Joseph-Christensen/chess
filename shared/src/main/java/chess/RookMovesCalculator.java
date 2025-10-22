package chess;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator extends PieceMovesCalculator {

    public RookMovesCalculator(ChessBoard board, ChessPosition position, ChessPiece piece) {
        super(board, position, piece);
    }

    @Override
    public Collection<ChessMove> returnMoves() {
        var moves = new HashSet<ChessMove>();
        int[][] directions = {{1,0},{0,1},{-1,0},{0,-1}};
        for (int[] direction : directions) {
            moves.addAll(directionalMoveCalc(direction[0], direction[1], board, position, piece.getTeamColor()));
        }
        return moves;
    }
}