package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator extends PieceMovesCalculator {

    public PawnMovesCalculator(ChessBoard board, ChessPosition position, ChessPiece piece) {
        super(board, position, piece);
    }

    @Override
    public Collection<ChessMove> returnMoves() {
        var moves = new HashSet<ChessMove>();
        ChessGame.TeamColor team = piece.getTeamColor();
        int row = position.getRow();
        int col = position.getColumn();
        int rowDir;

        if (team == ChessGame.TeamColor.WHITE) {
            rowDir = 1;
        }
        else {
            rowDir = -1;
        }

        boolean isPromotion = (team == ChessGame.TeamColor.WHITE && row == 7 || team == ChessGame.TeamColor.BLACK && row == 2);

        boolean isFirstMove = (team == ChessGame.TeamColor.WHITE && row == 2 || team == ChessGame.TeamColor.BLACK && row == 7);

        // capture
        moves.addAll(pawnMoveCalc(row + rowDir, col - 1, board, position, team, isPromotion, true));
        moves.addAll(pawnMoveCalc(row + rowDir, col + 1, board, position, team, isPromotion, true));

        // single move
        moves.addAll(pawnMoveCalc(row + rowDir, col, board, position, team, isPromotion, false));

        if (isFirstMove) {
            ChessPosition oneAhead = new ChessPosition(row + rowDir, col);
            if (board.getPiece(oneAhead) == null) {
                moves.addAll(pawnMoveCalc(row + (2 * rowDir), col, board, position, team, false, false));
            }
        }
        return moves;
    }
}