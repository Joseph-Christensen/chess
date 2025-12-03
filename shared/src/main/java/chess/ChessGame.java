package chess;

import com.google.gson.Gson;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard myBoard;
    private TeamColor teamTurn;
    private ChessPosition whiteKing;
    private ChessPosition blackKing;
    private boolean gameOver = false;
    private TeamColor resignedPlayer;

    public ChessGame() {
        myBoard = new ChessBoard();
        myBoard.resetBoard();
        findKings();
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

            if (!isInCheck(team)) {
                legalMoves.add(move);
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

        myBoard.addPiece(start, null);
        if (promote == null) {
            // not a promotion
            myBoard.addPiece(end, myPiece);
            if (myPiece.getPieceType() == ChessPiece.PieceType.KING) {
                if (color == TeamColor.WHITE) {
                    whiteKing = end;
                }
                else {
                    blackKing = end;
                }
            }
        }
        else {
            // promotion
            ChessPiece newPiece = new ChessPiece(color, promote);
            myBoard.addPiece(end, newPiece);
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
        TeamColor enemyColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        ChessPosition kingPos = (teamColor == TeamColor.WHITE) ? whiteKing : blackKing;

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currPosition = new ChessPosition(i, j);
                ChessPiece currPiece = myBoard.getPiece(currPosition);

                if (currPiece == null || currPiece.getTeamColor() != enemyColor) {
                    continue;
                }

                var moves = currPiece.pieceMoves(myBoard, currPosition);
                for (ChessMove move : moves) {
                    coveredPositions.add(move.getEndPosition());
                }
            }
        }
        return coveredPositions.contains(kingPos);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return anyValidMoves(teamColor);
        }
        return false;
    }
    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return anyValidMoves(teamColor);
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        myBoard = board;
        findKings();
    }

    private void findKings() {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition currPosition = new ChessPosition(i, j);
                ChessPiece currPiece = myBoard.getPiece(currPosition);

                if ((currPiece != null) && (currPiece.getPieceType() == ChessPiece.PieceType.KING)) {
                    if (currPiece.getTeamColor() == TeamColor.WHITE) {
                        whiteKing = currPosition;
                    }
                    else {
                        blackKing = currPosition;
                    }
                }
            }
        }
    }

    private boolean anyValidMoves(TeamColor teamColor) {
        var legalMoves = new HashSet<ChessMove>();
        if (teamColor == TeamColor.WHITE) {
            // white
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition currPos = new ChessPosition(i, j);
                    ChessPiece currPiece = myBoard.getPiece(currPos);
                    if ((currPiece != null) && (currPiece.getTeamColor() == TeamColor.WHITE)) {
                        legalMoves.addAll(validMoves(currPos));
                    }
                }
            }
        } else {
            // black
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition currPos = new ChessPosition(i, j);
                    ChessPiece currPiece = myBoard.getPiece(currPos);
                    if ((currPiece != null) && (currPiece.getTeamColor() == TeamColor.BLACK)) {
                        legalMoves.addAll(validMoves(currPos));
                    }
                }
            }
        }
        return legalMoves.isEmpty();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return myBoard;
    }

    public void endGame() {
        this.gameOver = true;
    }

    public void resign(TeamColor resignedPlayer) {
        this.resignedPlayer = resignedPlayer;
        this.gameOver = true;
    }

    public TeamColor getResignedPlayer() {
        return resignedPlayer;
    }

    public boolean isOver() {
        return gameOver;
    }

    public String getGameOverReason () {
        if (isInCheckmate(TeamColor.WHITE)) {
            return "White is already in checkmate.";
        } else if (isInCheckmate(TeamColor.BLACK)) {
            return "Black is already in checkmate.";
        } else if (isInStalemate(TeamColor.WHITE)) {
            return "White is already in stalemate.";
        } else if (isInStalemate(TeamColor.BLACK)) {
            return "Black is already in stalemate.";
        } else if (TeamColor.WHITE.equals(getResignedPlayer())) {
            return "White already resigned.";
        } else {
            return "Black already resigned.";
        }
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessGame chessGame = (ChessGame) o;
        return (
                Objects.equals(myBoard, chessGame.myBoard) &&
                teamTurn == chessGame.teamTurn &&
                Objects.equals(whiteKing, chessGame.whiteKing) &&
                Objects.equals(blackKing, chessGame.blackKing)
        );
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(myBoard);
        result = 31 * result + Objects.hashCode(teamTurn);
        result = 31 * result + Objects.hashCode(whiteKing);
        result = 31 * result + Objects.hashCode(blackKing);
        return result;
    }

    @Override
    public String toString() {
        var serializer = new Gson();
        return serializer.toJson(this);
    }
}
