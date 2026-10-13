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

    private ChessPosition whiteKingPos = new ChessPosition(1, 5);
    private ChessPosition blackKingPos = new ChessPosition(8, 5);
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
            gameBoard.removePiece(move.getStartPosition());
            if (!isInCheck(gameBoard.getPiece(move.getEndPosition()).getTeamColor())) {
                vMoves.add(move);

            }
            gameBoard = new ChessBoard(tmpBoard);
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
        if (startPiece.getTeamColor() != currentTeam) {
            throw new InvalidMoveException("Invalid Move: Not teams turn");
        }
        Collection<ChessMove> potentialMove;
        potentialMove = validMoves(start);
        if (potentialMove.contains(move)) {
            ChessPiece pieceToMove;
            if (move.getPromotionPiece() != null) {
                pieceToMove = new ChessPiece(startPiece.getTeamColor(), move.getPromotionPiece());
            } else {
                pieceToMove = startPiece;
            }
            ChessPosition end = move.getEndPosition();
            gameBoard.addPiece(end, pieceToMove);
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
     * Finds a given teams king and updates its position
     *
     * @param teamColor which team to find the king for
     */
    private void findKing(TeamColor teamColor) {
        //scans each row and col for the king
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition tmpPos = new ChessPosition(row, col);
                if (gameBoard.getPiece(tmpPos) != null) {
                    if (KING == gameBoard.getPiece(tmpPos).getPieceType()) {
                        if (gameBoard.getPiece(tmpPos).getTeamColor() == teamColor) {
                            if (gameBoard.getPiece(tmpPos).getTeamColor() == WHITE) {
                                whiteKingPos = tmpPos;
                            } else {
                                blackKingPos = tmpPos;
                            }
                            return;
                        }
                    }
                }
            }
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
        findKing(teamColor);

        Rules rule = new Rules();
        for (ChessPiece.PieceType type : ChessPiece.PieceType.values()) {
            Collection<ChessMove> checkMoves;
            if (teamColor == WHITE) {
                checkMoves = rule.pieceRule(type).moves(gameBoard, whiteKingPos);
            } else {
                checkMoves = rule.pieceRule(type).moves(gameBoard, blackKingPos);
            }
            for (ChessMove move : checkMoves) {
                if (gameBoard.getPiece(move.getEndPosition()) != null) {
                    if ((gameBoard.getPiece(move.getEndPosition()).getPieceType() == type) && (gameBoard.getPiece(move.getEndPosition()).getTeamColor() != teamColor)) {
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
        findKing(teamColor);

        if (!(isInCheck(teamColor))) {
            return false;
        }
        Collection<ChessMove> possibleMoves;
        Rules rules = new Rules();
        if (teamColor == WHITE) {
            possibleMoves = rules.pieceRule(KING).moves(gameBoard, whiteKingPos);
        } else {
            possibleMoves = rules.pieceRule(KING).moves(gameBoard, whiteKingPos);
        }

        Collection<ChessMove> moveToRemove = new HashSet<>();
        for (ChessMove move : possibleMoves) {
            ChessBoard tmpBoard = new ChessBoard(gameBoard);
            ChessPosition start = move.getStartPosition();
            ChessPosition end = move.getEndPosition();
            gameBoard.addPiece(start, gameBoard.getPiece(end));
            gameBoard.removePiece(start);
            if (isInCheck(teamColor)) {
                moveToRemove.add(move);
            }
            gameBoard = new ChessBoard(tmpBoard);
        }
        for (ChessMove move : moveToRemove) {
            possibleMoves.remove(move);
        }
        return possibleMoves.isEmpty();


    }

    private boolean teamSave(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = gameBoard.getPiece(pos);
                if ((piece == null) || (piece.getTeamColor() != teamColor) || (piece.getPieceType() == KING)) {
                    continue;
                }
                Collection<ChessMove> checkMoves = piece.pieceMoves(gameBoard, pos);
                Collection<ChessMove> canMoves = new HashSet<>();
                for (ChessMove move : checkMoves) {
                    ChessBoard tmpBoard = new ChessBoard(gameBoard);
                    gameBoard.addPiece(move.getStartPosition(), piece);
                    if (!(isInCheck(currentTeam))) {
                        canMoves.add(move);
                    }
                    gameBoard = new ChessBoard(tmpBoard);
                }
                if (!(canMoves.isEmpty())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = gameBoard.getPiece(pos);
                if ((piece == null) || (piece.getTeamColor() != teamColor)) {
                    continue;
                }
                Collection<ChessMove> vMoves = validMoves(pos);
                if (!(vMoves.isEmpty())) {
                    return false;
                }
            }
        }
        return true;
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
        return Objects.equals(whiteKingPos, chessGame.whiteKingPos) && Objects.equals(blackKingPos, chessGame.blackKingPos) && Objects.equals(gameBoard, chessGame.gameBoard) && currentTeam == chessGame.currentTeam;
    }

    @Override
    public int hashCode() {
        return Objects.hash(whiteKingPos, blackKingPos, gameBoard, currentTeam);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE, BLACK
    }
}