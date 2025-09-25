package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece piece;

    PawnMovesCalculator(ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board = board;
        this.position = position;
        this.piece = piece;
    }

    public Collection<ChessMove> returnMoves() {
        var moves = new HashSet<ChessMove>();
        ChessGame.TeamColor team = piece.getTeamColor();
        int initRow = position.getRow();

        if (team == ChessGame.TeamColor.WHITE) {
            // white pawn, moves up
            if (initRow == 7) {
                // promotion
                for (int i = 0; i < 3; i++) {
                    int row = position.getRow();
                    int col = position.getColumn();

                    if (i == 0) {
                        // capture left
                        row++;
                        col--;
                        if (row < 9 && col > 0) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) != null && board.getPiece(currPos).getTeamColor() != team) {
                                // can move
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.KNIGHT));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.BISHOP));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.ROOK));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.QUEEN));
                            }
                        }
                    }
                    else if (i == 1) {
                        // capture right
                        row++;
                        col++;
                        if (row < 9 && col < 9) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) != null && board.getPiece(currPos).getTeamColor() != team) {
                                // can move
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.KNIGHT));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.BISHOP));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.ROOK));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.QUEEN));
                            }
                        }
                    }
                    else {
                        //move up one
                        row++;
                        if (row < 9) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) == null) {
                                // can move
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.KNIGHT));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.BISHOP));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.ROOK));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.QUEEN));
                            }
                        }
                    }
                }
            }
            else if (initRow == 2) {
                // option to double move
                for (int i = 0; i < 3; i++) {
                    int row = position.getRow();
                    int col = position.getColumn();

                    if (i == 0) {
                        // capture left
                        row++;
                        col--;
                        if (row < 9 && col > 0) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) != null && board.getPiece(currPos).getTeamColor() != team) {
                                // can move
                                moves.add(new ChessMove(position, currPos, null));
                            }
                        }
                    }
                    else if (i == 1) {
                        // capture right
                        row++;
                        col++;
                        if (row < 9 && col < 9) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) != null && board.getPiece(currPos).getTeamColor() != team) {
                                // can move
                                moves.add(new ChessMove(position, currPos, null));
                            }
                        }
                    }
                    else {
                        //move up one
                        row++;
                        if (row < 9) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) == null) {
                                // can move
                                moves.add(new ChessMove(position, currPos, null));
                                // check for a double move possibility
                                row++;
                                ChessPosition newPos = new ChessPosition(row, col);
                                if (board.getPiece(newPos) == null) {
                                    // add again
                                    moves.add(new ChessMove(position, newPos, null));
                                }
                            }
                        }
                    }
                }
            }
            else {
                // normal
                for (int i = 0; i < 3; i++) {
                    int row = position.getRow();
                    int col = position.getColumn();

                    if (i == 0) {
                        // capture left
                        row++;
                        col--;
                        if (row < 9 && col > 0) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) != null && board.getPiece(currPos).getTeamColor() != team) {
                                // can move
                                moves.add(new ChessMove(position, currPos, null));
                            }
                        }
                    }
                    else if (i == 1) {
                        // capture right
                        row++;
                        col++;
                        if (row < 9 && col < 9) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) != null && board.getPiece(currPos).getTeamColor() != team) {
                                // can move
                                moves.add(new ChessMove(position, currPos, null));
                            }
                        }
                    }
                    else {
                        //move up one
                        row++;
                        if (row < 9) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) == null) {
                                // can move
                                moves.add(new ChessMove(position, currPos, null));
                            }
                        }
                    }
                }
            }
        }
        else {
            // black pawn, moves down
            if (initRow == 2) {
                // promotion
                for (int i = 0; i < 3; i++) {
                    int row = position.getRow();
                    int col = position.getColumn();

                    if (i == 0) {
                        // capture left
                        row--;
                        col--;
                        if (row > 0 && col > 0) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) != null && board.getPiece(currPos).getTeamColor() != team) {
                                // can move
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.KNIGHT));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.BISHOP));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.ROOK));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.QUEEN));
                            }
                        }
                    }
                    else if (i == 1) {
                        // capture right
                        row--;
                        col++;
                        if (row > 0 && col < 9) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) != null && board.getPiece(currPos).getTeamColor() != team) {
                                // can move
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.KNIGHT));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.BISHOP));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.ROOK));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.QUEEN));
                            }
                        }
                    }
                    else {
                        //move up one
                        row--;
                        if (row > 0) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) == null) {
                                // can move
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.KNIGHT));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.BISHOP));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.ROOK));
                                moves.add(new ChessMove(position, currPos, ChessPiece.PieceType.QUEEN));
                            }
                        }
                    }
                }
            }
            else if (initRow == 7) {
                // option to double move
                for (int i = 0; i < 3; i++) {
                    int row = position.getRow();
                    int col = position.getColumn();

                    if (i == 0) {
                        // capture left
                        row--;
                        col--;
                        if (row > 0 && col > 0) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) != null && board.getPiece(currPos).getTeamColor() != team) {
                                // can move
                                moves.add(new ChessMove(position, currPos, null));
                            }
                        }
                    }
                    else if (i == 1) {
                        // capture right
                        row--;
                        col++;
                        if (row > 0 && col < 9) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) != null && board.getPiece(currPos).getTeamColor() != team) {
                                // can move
                                moves.add(new ChessMove(position, currPos, null));
                            }
                        }
                    }
                    else {
                        //move up one
                        row--;
                        if (row > 0) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) == null) {
                                // can move
                                moves.add(new ChessMove(position, currPos, null));
                                // check for a double move possibility
                                row--;
                                ChessPosition newPos = new ChessPosition(row, col);
                                if (board.getPiece(newPos) == null) {
                                    // add again
                                    moves.add(new ChessMove(position, newPos, null));
                                }
                            }
                        }
                    }
                }
            }
            else {
                // normal
                for (int i = 0; i < 3; i++) {
                    int row = position.getRow();
                    int col = position.getColumn();

                    if (i == 0) {
                        // capture left
                        row--;
                        col--;
                        if (row > 0 && col > 0) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) != null && board.getPiece(currPos).getTeamColor() != team) {
                                // can move
                                moves.add(new ChessMove(position, currPos, null));
                            }
                        }
                    }
                    else if (i == 1) {
                        // capture right
                        row--;
                        col++;
                        if (row > 0 && col < 9) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) != null && board.getPiece(currPos).getTeamColor() != team) {
                                // can move
                                moves.add(new ChessMove(position, currPos, null));
                            }
                        }
                    }
                    else {
                        //move up one
                        row--;
                        if (row > 0) {
                            // check for piece
                            ChessPosition currPos = new ChessPosition(row, col);
                            if (board.getPiece(currPos) == null) {
                                // can move
                                moves.add(new ChessMove(position, currPos, null));
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }
}
