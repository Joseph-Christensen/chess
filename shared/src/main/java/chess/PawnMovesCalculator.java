package chess;

import java.util.Collection;

public class PawnMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece piece;

    PawnMovesCalculator (ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board = board;
        this.position = position;
        this.piece = piece;
    }

    public Collection<ChessMove> returnMoves() {
        return null;
    }
}
