package chess;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private Map<ChessPiece, ChessPosition> whitePieces = new HashMap<>();
    private Map<ChessPiece, ChessPosition> blackPieces = new HashMap<>();
    private ChessBoard myBoard;
    private TeamColor teamTurn;
    private ChessPosition whiteKing;
    private ChessPosition blackKing;

    public ChessGame() {
        myBoard = new ChessBoard();
        myBoard.resetBoard();
        processBoardPieces();
        setTeamTurn(TeamColor.WHITE);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessBoard copyBoard = myBoard.copy();
        // making a copy board so I can make changes to myBoard and it won't affect the game
        var legalMoves = new HashSet<ChessMove>();
        ChessPiece myPiece = myBoard.getPiece(startPosition);
        TeamColor team = myPiece.getTeamColor();
        var moves = myPiece.pieceMoves(myBoard, startPosition);

        for (ChessMove move : moves) {
            ChessPosition start = move.getStartPosition();
            ChessPosition end = move.getEndPosition();

            ChessPiece destinationPiece = myBoard.getPiece(end);

            myBoard.addPiece(end, myPiece);
            myBoard.addPiece(start, null);

            if (myPiece.getPieceType() == ChessPiece.PieceType.KING) {
                if (team == TeamColor.WHITE) {
                    whiteKing = end;
                }
                else {
                    blackKing = end;
                }
            }

            if (destinationPiece != null) {
                if (team == TeamColor.WHITE) {
                    blackPieces.remove(destinationPiece);
                    if (!isInCheck(team)) {
                        legalMoves.add(move);
                    }
                    blackPieces.put(destinationPiece, end);
                }
                else {
                    whitePieces.remove(destinationPiece);
                    if (!isInCheck(team)) {
                        legalMoves.add(move);
                    }
                    whitePieces.put(destinationPiece, end);
                }
            }
            else {
                if (!isInCheck(team)) {
                    legalMoves.add(move);
                }
            }

            if (myPiece.getPieceType() == ChessPiece.PieceType.KING) {
                if (team == TeamColor.WHITE) {
                    whiteKing = start;
                }
                else {
                    blackKing = start;
                }
            }

            myBoard = copyBoard.copy();
        }
        return legalMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promote = move.getPromotionPiece();
        ChessPiece myPiece = myBoard.getPiece(start);
        TeamColor color = getTeamTurn();

        if (myPiece == null) {
            throw new InvalidMoveException("Not a Piece");
        }

        if (myPiece.getTeamColor() != color) {
            throw new InvalidMoveException("Wrong Turn");
        }

        var legalMoves = validMoves(start);
        if (!legalMoves.contains(move)) {
            throw new InvalidMoveException("Invalid Move: " + move);
        }

        // check to see if there was a piece there
        ChessPiece destinationPiece = myBoard.getPiece(end);
        if (destinationPiece != null) {
            if (color == TeamColor.WHITE) {
                whitePieces.remove(destinationPiece);
            }
            else {
                blackPieces.remove(destinationPiece);
            }
        }


        myBoard.addPiece(start, null);
        if (promote == null) {
            // not a promotion
            myBoard.addPiece(end, myPiece);
            if (color == TeamColor.WHITE) {
                // white
                whitePieces.put(myPiece, end);
                if (myPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    whiteKing = end;
                }
            }
            else {
                // black
                blackPieces.put(myPiece, end);
                if (myPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    blackKing = end;
                }
            }
        }
        else {
            // promotion
            ChessPiece newPiece = new ChessPiece(color, promote);
            myBoard.addPiece(end, newPiece);
            if (color == TeamColor.WHITE) {
                // white
                whitePieces.remove(myPiece);
                whitePieces.put(newPiece, end);
            }
            else {
                // black
                blackPieces.remove(myPiece);
                blackPieces.put(newPiece, end);
            }
        }
        if (color == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
        }
        else {
            setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        var coveredPositions = new HashSet<ChessPosition>();
        if (teamColor == TeamColor.WHITE) {
            // white
            for (Map.Entry<ChessPiece, ChessPosition> entry : blackPieces.entrySet()) {
                ChessPiece currPiece = entry.getKey();
                ChessPosition currPos = entry.getValue();
                var currMoves = currPiece.pieceMoves(myBoard, currPos);
                for (ChessMove move : currMoves) {
                    coveredPositions.add(move.getEndPosition());
                }
            }
            return coveredPositions.contains(whiteKing);
        }
        else {
            // black
            for (Map.Entry<ChessPiece, ChessPosition> entry : whitePieces.entrySet()) {
                ChessPiece currPiece = entry.getKey();
                ChessPosition currPos = entry.getValue();
                var currMoves = currPiece.pieceMoves(myBoard, currPos);
                for (ChessMove move : currMoves) {
                    coveredPositions.add(move.getEndPosition());
                }
            }
            return coveredPositions.contains(blackKing);
        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        myBoard = board;
        processBoardPieces();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return myBoard;
    }

    private void processBoardPieces() {
        whitePieces.clear();
        blackPieces.clear();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition currPos = new ChessPosition(i, j);
                ChessPiece currPiece = myBoard.getPiece(currPos);
                if (currPiece == null) {
                    continue;
                }
                TeamColor color = currPiece.getTeamColor();
                if (color == TeamColor.WHITE) {
                    whitePieces.put(currPiece, currPos);
                    if (currPiece.getPieceType() == ChessPiece.PieceType.KING) {
                        whiteKing = currPos;
                    }
                }
                else {
                    blackPieces.put(currPiece, currPos);
                    if (currPiece.getPieceType() == ChessPiece.PieceType.KING) {
                        blackKing = currPos;
                    }
                }
            }
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
