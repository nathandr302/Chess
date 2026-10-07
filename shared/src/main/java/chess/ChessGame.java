package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.KING;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessPosition whiteKing = new ChessPosition(1, 5);
    private ChessPosition blackKing = new ChessPosition(8, 5);
    private ChessBoard gameBoard = new ChessBoard();
    private TeamColor currentTeam = WHITE;


    public ChessGame() {
        gameBoard.resetBoard();
    }


    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeam = team;
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (gameBoard.getPiece(startPosition) == null) {
            return null;
        }
        Collection<ChessMove> vMoves = new HashSet<>();
        Collection<ChessMove> moves = gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);


        for (ChessMove move : moves) {
            ChessBoard tmpBoard = new ChessBoard(gameBoard);
            gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(startPosition));
            if (!(isInCheck(gameBoard.getPiece(move.getStartPosition()).getTeamColor()))) {
                vMoves.add(move);

            }
            gameBoard = tmpBoard;
        }
        return vMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (move == null) {
            throw new InvalidMoveException("Invalid Move: move is empty");
        }
        ChessPosition start = move.getStartPosition();
        ChessPiece startPiece = gameBoard.getPiece(start);
        if (startPiece == null) {
            throw new InvalidMoveException("Invalid Move: No piece at start position");
        }
        if (startPiece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Invalid Move: Not teams turn");
        }
        Collection<ChessMove> potentialMove;
        potentialMove = validMoves(start);
        if (potentialMove.contains(move)) {
            ChessPosition end = move.getEndPosition();
            gameBoard.addPiece(end, startPiece);
            gameBoard.removePiece(start);
            if (currentTeam == BLACK) {
                currentTeam = WHITE;
            } else {
                currentTeam = BLACK;
            }
        } else {
            throw new InvalidMoveException("Invalid Move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // updates the king's position
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition tmpPos = new ChessPosition(row, col);
                if (gameBoard.getPiece(tmpPos) != null) {
                    if (KING == gameBoard.getPiece(tmpPos).getPieceType()) {
                        if (gameBoard.getPiece(tmpPos).getTeamColor() == WHITE) {
                            whiteKing = tmpPos;
                        } else {
                            blackKing = tmpPos;
                        }
                    }
                }
            }
        }
        Rules rule = new Rules();
        for (ChessPiece.PieceType type : ChessPiece.PieceType.values()) {
            Collection<ChessMove> checkMoves;
            if (teamColor == WHITE) {
                checkMoves = rule.pieceRule(type).moves(gameBoard, whiteKing);
            } else {
                checkMoves = rule.pieceRule(type).moves(gameBoard, blackKing);
            }
            for (ChessMove move : checkMoves) {
                if (gameBoard.getPiece(move.getEndPosition()) != null) {
                    if ((gameBoard.getPiece(move.getEndPosition()).getPieceType() == type) &&
                            (gameBoard.getPiece(move.getEndPosition()).getTeamColor() != teamColor)) {
                        return true;
                    }
                }
            }
        }

        return false;
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
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(gameBoard);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }
}