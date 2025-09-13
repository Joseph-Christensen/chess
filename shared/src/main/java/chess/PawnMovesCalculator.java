package chess;

import java.util.Collection;
import java.util.HashSet;

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
        var moves = new HashSet<ChessMove>();
        ChessGame.TeamColor pieceColor = piece.getTeamColor();
        int initRow = position.getRow();

        if (pieceColor == ChessGame.TeamColor.WHITE) {
            // pawn is white, will be advancing in rows
            if (initRow == 2) {
                // 3 options, capture left, capture right, up 1 and 2
                for (int i = 0; i < 3; i++) {
                    // runs though 3 times for each option
                    int row = position.getRow();
                    int col = position.getColumn();
                    if (i == 0) {
                        // capture left
                        col--;
                        row++;
                        if (col > 0 && row < 9) {
                            if (board.getPiece(new ChessPosition(row, col)) != null) {
                                ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                                if (pieceColor != currPiece.getTeamColor()) {
                                    // valid capture
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                                }
                            }
                        }
                    }
                    else if (i == 1) {
                        // capture right
                        col++;
                        row++;
                        if (col < 9 && row < 9) {
                            if (board.getPiece(new ChessPosition(row, col)) != null) {
                                ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                                if (pieceColor != currPiece.getTeamColor()) {
                                    // valid capture
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                                }
                            }
                        }
                    }
                    else {
                        // up
                        row++;
                        if (row < 9) {
                            if (board.getPiece(new ChessPosition(row, col)) == null) {
                                // no piece detected
                                moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                                // checking if up 2 is valid
                                row++;
                                if (board.getPiece(new ChessPosition(row, col)) == null) {
                                    // both up 1 and up 2 valid
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                                }
                            }
                        }
                    }
                }
            }
            else if (initRow == 7) {
                // will promote, capture left, capture right, up 1, all with 4 promote options
                for (int i = 0; i < 3; i++) {
                    // runs though 3 times for each option
                    int row = position.getRow();
                    int col = position.getColumn();
                    if (i == 0) {
                        // capture left
                        col--;
                        row++;
                        if (col > 0 && row < 9) {
                            if (board.getPiece(new ChessPosition(row, col)) != null) {
                                ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                                if (pieceColor != currPiece.getTeamColor()) {
                                    // valid capture
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.KNIGHT));
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.BISHOP));
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.ROOK));
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.QUEEN));
                                }
                            }
                        }
                    }
                    else if (i == 1) {
                        // capture right
                        col++;
                        row++;
                        if (col < 9 && row < 9) {
                            if (board.getPiece(new ChessPosition(row, col)) != null) {
                                ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                                if (pieceColor != currPiece.getTeamColor()) {
                                    // valid capture
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.KNIGHT));
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.BISHOP));
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.ROOK));
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.QUEEN));
                                }
                            }
                        }
                    }
                    else {
                        // up
                        row++;
                        if (row < 9) {
                            if (board.getPiece(new ChessPosition(row, col)) == null) {
                                // no piece detected
                                moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.KNIGHT));
                                moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.BISHOP));
                                moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.ROOK));
                                moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.QUEEN));
                            }
                        }
                    }
                }
            }
            else {
                // 3 options, capture left, capture right, up 1
                for (int i = 0; i < 3; i++) {
                    // runs though 3 times for each option
                    int row = position.getRow();
                    int col = position.getColumn();
                    if (i == 0) {
                        // capture left
                        col--;
                        row++;
                        if (col > 0 && row < 9) {
                            if (board.getPiece(new ChessPosition(row, col)) != null) {
                                ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                                if (pieceColor != currPiece.getTeamColor()) {
                                    // valid capture
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                                }
                            }
                        }
                    }
                    else if (i == 1) {
                        // capture right
                        col++;
                        row++;
                        if (col < 9 && row < 9) {
                            if (board.getPiece(new ChessPosition(row, col)) != null) {
                                ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                                if (pieceColor != currPiece.getTeamColor()) {
                                    // valid capture
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                                }
                            }
                        }
                    }
                    else {
                        // up
                        row++;
                        if (row < 9) {
                            if (board.getPiece(new ChessPosition(row, col)) == null) {
                                // no piece detected
                                moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                            }
                        }
                    }
                }
            }
        }
        else {
            // pawn is black, will be decreasing in rows
            if (initRow == 7) {
                // 3 options, capture left, capture right, down 1 and 2
                for (int i = 0; i < 3; i++) {
                    // runs though 3 times for each option
                    int row = position.getRow();
                    int col = position.getColumn();
                    if (i == 0) {
                        // capture left
                        col--;
                        row--;
                        if (col > 0 && row > 0) {
                            if (board.getPiece(new ChessPosition(row, col)) != null) {
                                ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                                if (pieceColor != currPiece.getTeamColor()) {
                                    // valid capture
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                                }
                            }
                        }
                    }
                    else if (i == 1) {
                        // capture right
                        col++;
                        row--;
                        if (col < 9 && row > 0) {
                            if (board.getPiece(new ChessPosition(row, col)) != null) {
                                ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                                if (pieceColor != currPiece.getTeamColor()) {
                                    // valid capture
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                                }
                            }
                        }
                    }
                    else {
                        // down
                        row--;
                        if (row < 9) {
                            if (board.getPiece(new ChessPosition(row, col)) == null) {
                                // no piece detected
                                moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                                // checking if down 2 is valid
                                row--;
                                if (board.getPiece(new ChessPosition(row, col)) == null) {
                                    // both down 1 and down 2 valid
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                                }
                            }
                        }
                    }
                }
            }
            else if (initRow == 2) {
                // will promote, capture left, capture right, down 1, all with 4 promote options
                for (int i = 0; i < 3; i++) {
                    // runs though 3 times for each option
                    int row = position.getRow();
                    int col = position.getColumn();
                    if (i == 0) {
                        // capture left
                        col--;
                        row--;
                        if (col > 0 && row > 0) {
                            if (board.getPiece(new ChessPosition(row, col)) != null) {
                                ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                                if (pieceColor != currPiece.getTeamColor()) {
                                    // valid capture
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.KNIGHT));
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.BISHOP));
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.ROOK));
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.QUEEN));
                                }
                            }
                        }
                    }
                    else if (i == 1) {
                        // capture right
                        col++;
                        row--;
                        if (col < 9 && row > 0) {
                            if (board.getPiece(new ChessPosition(row, col)) != null) {
                                ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                                if (pieceColor != currPiece.getTeamColor()) {
                                    // valid capture
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.KNIGHT));
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.BISHOP));
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.ROOK));
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.QUEEN));
                                }
                            }
                        }
                    }
                    else {
                        // down
                        row--;
                        if (row > 0) {
                            if (board.getPiece(new ChessPosition(row, col)) == null) {
                                // no piece detected
                                moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.KNIGHT));
                                moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.BISHOP));
                                moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.ROOK));
                                moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.QUEEN));
                            }
                        }
                    }
                }
            }
            else {
                // 3 options, capture left, capture right, down 1
                for (int i = 0; i < 3; i++) {
                    // runs though 3 times for each option
                    int row = position.getRow();
                    int col = position.getColumn();
                    if (i == 0) {
                        // capture left
                        col--;
                        row--;
                        if (col > 0 && row > 0) {
                            if (board.getPiece(new ChessPosition(row, col)) != null) {
                                ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                                if (pieceColor != currPiece.getTeamColor()) {
                                    // valid capture
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                                }
                            }
                        }
                    }
                    else if (i == 1) {
                        // capture right
                        col++;
                        row--;
                        if (col < 9 && row > 0) {
                            if (board.getPiece(new ChessPosition(row, col)) != null) {
                                ChessPiece currPiece = board.getPiece(new ChessPosition(row, col));
                                if (pieceColor != currPiece.getTeamColor()) {
                                    // valid capture
                                    moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                                }
                            }
                        }
                    }
                    else {
                        // down
                        row--;
                        if (row > 0) {
                            if (board.getPiece(new ChessPosition(row, col)) == null) {
                                // no piece detected
                                moves.add(new ChessMove(new ChessPosition((position.getRow()), position.getColumn()), new ChessPosition(row, col), null));
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }
}
