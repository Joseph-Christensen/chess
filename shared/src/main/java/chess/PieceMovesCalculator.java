package chess;

import java.util.Collection;
import java.util.HashSet;

public class PieceMovesCalculator {

    protected final ChessBoard board;
    protected final ChessPosition position;
    protected final ChessPiece piece;

    protected PieceMovesCalculator(ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board = board;
        this.position = position;
        this.piece = piece;
    }

    public Collection<ChessMove> returnMoves() {
        return new HashSet<>();
    }

    protected Collection<ChessMove> directionalMoveCalc(int rowDir, int colDir, ChessBoard board, ChessPosition startPos, ChessGame.TeamColor team) {
        var moves = new HashSet<ChessMove>();
        int row = startPos.getRow() + rowDir;
        int col = startPos.getColumn() + colDir;

        while (true) {
            if (row < 1 || row > 8 || col < 1 || col > 8) {
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

    protected ChessMove positionMoveCalc(int rowDir, int colDir, ChessBoard board, ChessPosition startPos, ChessGame.TeamColor team) {
        int row = startPos.getRow() + rowDir;
        int col = startPos.getColumn() + colDir;

        if (row < 1 || row > 8 || col < 1 || col > 8) {
            return null;
        }
        ChessPosition endPos = new ChessPosition(row, col);

        if (board.getPiece(endPos) == null) {
            // no piece
            return new ChessMove(startPos, endPos, null);
        }
        else {
            // piece detected
            if (board.getPiece(endPos).getTeamColor() != team) {
                // different team
                return new ChessMove(startPos, endPos, null);
            } else {
                return null;
            }
        }
    }

    protected Collection<ChessMove> collectSingleStepMoves(int[][] directions) {
        var moves = new HashSet<ChessMove>();
        for (int[] direction : directions) {
            var move = positionMoveCalc(direction[0], direction[1], board, position, piece.getTeamColor());
            if (move != null) {
                moves.add(move);
            }
        }
        return moves;
    }

    protected Collection<ChessMove> pawnMoveCalc(
            int row, int col, ChessBoard board,
            ChessPosition startPos, ChessGame.TeamColor team,
            boolean promotion, boolean isCapture) {
        var moves = new HashSet<ChessMove>();

        if (row < 1 || row > 8 || col < 1 || col > 8) {
            return moves;
        }

        ChessPosition endPos = new ChessPosition(row, col);
        ChessPiece targetPiece = board.getPiece(endPos);

        if (!isCapture && targetPiece != null) {
            return moves;
        }

        if (isCapture && (targetPiece == null || targetPiece.getTeamColor() == team)) {
            return moves;
        }

        if (promotion) {
            moves.add(new ChessMove(startPos, endPos, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(startPos, endPos, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(startPos, endPos, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(startPos, endPos, ChessPiece.PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(startPos, endPos, null));
        }
        return moves;
    }
}
