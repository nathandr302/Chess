package chess;

import java.util.Collection;
import java.util.HashSet;


public class PawnMovementRule extends BaseMovementRule {
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {

        var row = position.getRow();
        var col = position.getColumn();
        var moves = new HashSet<ChessMove>();

        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK) {
            var moveDirection = -1;

        }
        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            var moveDirection = 1;

        }


        return moves;
    }

    private void PawnHelper(HashSet<ChessMove> moves, ChessPosition position, int moveDirection) {
    }
}
