package chess;

import java.util.Collection;
import java.util.HashSet;


public class PawnMovementRule extends BaseMovementRule {
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {

        var moves = new HashSet<ChessMove>();
        var currentRow = position.getRow();
        var currentCol = position.getColumn();
        int rowDirection = 0;
        int rowStart = 0;
        int proRow = 0;

        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK) {
            rowDirection = -1;
            rowStart = 7;
            proRow = 1;
        } else if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            rowDirection = 1;
            rowStart = 2;
            proRow = 8;
        }
        // move forward
        if (!(board.getPiece(new ChessPosition(currentRow + rowDirection, currentCol)) instanceof ChessPiece)) {
            calculateMoves(board, position, rowDirection, 0, moves, false);
            if ((currentRow == rowStart) && !(board.getPiece(new ChessPosition(currentRow + (2 * rowDirection), currentCol)) instanceof ChessPiece)) {
                calculateMoves(board, position, 2 * rowDirection, 0, moves, false);
            }
        }
        //captures
        if (currentCol + 1 <= 8) {
            if (board.getPiece(new ChessPosition(currentRow + rowDirection, currentCol + 1)) instanceof ChessPiece) {
                calculateMoves(board, position, rowDirection, 1, moves, false);
            }
        }
        if (currentCol - 1 >= 1) {
            if (board.getPiece(new ChessPosition(currentRow + rowDirection, currentCol - 1)) instanceof ChessPiece) {
                calculateMoves(board, position, rowDirection, -1, moves, false);
            }
        }
        return moves;
    }
}
