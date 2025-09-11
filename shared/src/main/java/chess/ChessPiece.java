package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Bishop Moves Calculator
     */

    public Collection<ChessMove> bishopMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        var moves = new HashSet<ChessMove>();
        for (int i = 0; i < 4; i++) {
            // runs though 4 times for each bishop direction, up-left, up-right, down-right, down-left
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            if (i == 0) {
                // up-left
                while (true) {
                    row++;
                    if (row > 8) {
                        break;
                    }
                    col--;
                    if (col < 1) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor == currPiece.getTeamColor()) {
                            // same team
                            break;
                        }
                        else {
                            // other team
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
            else if (i == 1) {
                // up-right
                while (true) {
                    row++;
                    if (row > 8) {
                        break;
                    }
                    col++;
                    if (col > 8) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor == currPiece.getTeamColor()) {
                            // same team
                            break;
                        }
                        else {
                            // other team
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
            else if (i == 2) {
                // down-right
                while (true) {
                    row--;
                    if (row < 1) {
                        break;
                    }
                    col++;
                    if (col > 8) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor == currPiece.getTeamColor()) {
                            // same team
                            break;
                        }
                        else {
                            // other team
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
            else {
                // down-left
                while (true) {
                    row--;
                    if (row < 1) {
                        break;
                    }
                    col--;
                    if (col < 1) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor == currPiece.getTeamColor()) {
                            // same team
                            break;
                        }
                        else {
                            // other team
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
        }
        return moves;
    }


    /**
     * Rook Moves Calculator
     */

    public Collection<ChessMove> rookMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        var moves = new HashSet<ChessMove>();
        for (int i = 0; i < 4; i++) {
            // runs though 4 times for each rook direction, left, up, right, down
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            if (i == 0) {
                // left
                while (true) {
                    col--;
                    if (col < 1) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor == currPiece.getTeamColor()) {
                            // same team
                            break;
                        }
                        else {
                            // other team
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
            else if (i == 1) {
                // up
                while (true) {
                    row++;
                    if (row > 8) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor == currPiece.getTeamColor()) {
                            // same team
                            break;
                        }
                        else {
                            // other team
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
            else if (i == 2) {
                // right
                while (true) {
                    col++;
                    if (col > 8) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor == currPiece.getTeamColor()) {
                            // same team
                            break;
                        }
                        else {
                            // other team
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
            else {
                // down
                while (true) {
                    row--;
                    if (row < 1) {
                        break;
                    }
                    if (board.getPiece(new ChessPosition(row, col)) == null) {
                        // keep going
                        moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                    }
                    else {
                        // piece detected, check for color
                        ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                        if (pieceColor == currPiece.getTeamColor()) {
                            // same team
                            break;
                        }
                        else {
                            // other team
                            moves.add(new ChessMove(new ChessPosition((myPosition.getRow()), myPosition.getColumn()), new ChessPosition(row, col), null));
                            break;
                        }
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        // var moves = new HashSet<ChessMove>();
        if (piece.getPieceType() == PieceType.BISHOP) {
            // moves.add(new ChessMove(new ChessPosition(5,4), new ChessPosition(6,5), null));
            return bishopMovesCalculator(board, myPosition);
        }
        else if (piece.getPieceType() == PieceType.ROOK) {
            return rookMovesCalculator(board, myPosition);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(pieceColor);
        result = 31 * result + Objects.hashCode(type);
        return result;
    }
}
