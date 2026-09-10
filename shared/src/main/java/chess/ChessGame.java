package chess;

import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {

        throw new RuntimeException("Not implemented");
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * ALL pieces
     *      all pieces must be with in the given range of the playing board (ie. 0 -> 7 OR 1->8) starting and ending positions must be with in that range.
     *      if 0 <= (startRow OR startCol OR endRow OR endCOl) <= 7, then return valid move, else throw(invalid move)
     *
     *      then we need to check if the starting position of the piece and the ending position of the piece are the same,
     *
     *      if startPosition == endPosition, then throw invalidMove,
     *
     *
     * Bishop move type
     *      valid move = endPosistion-startPosition then take the difference of the rows and the difference of the cols and divide them (rowDif/colDif)
     *          if the absolute value of that differnece is equal to 1 then it is a valid move, if in any other case, like divide by zero or the abs is not equal to 1 then throw(invalid move)
     *
     *      if abs((endRow-startRow)/(endCol-StartCol)) == 1 then move is valid, else throw(invalidMove)
     *      --(put this in a try catch??)
     *
 *   * Rook move type
     *      A valid move would be if the startRow is equal to endRow and the cols changes OR startCol and endCol are equal to each other while the row changes
     *
     *      if startRow == endRow, then valid move, else throw(invalidMove)
     *      if startCol == endCol, then valid move, else throw(invalidMove)
     *
     * Queen move type
     *      A Queen inhariets moved from both Bishops AND Rooks. however it is either the Rook or the Bishop moveset not both at the same time.
     *
     * King move Type
     *
     * Pawn move type
     *
     * Knight move type
     *
     *
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");

    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        throw new RuntimeException("Not implemented");
        /**

         */
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
    public boolean isInStalemate(TeamColor teamColor)
    {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {

        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {

        throw new RuntimeException("Not implemented");
    }
}
