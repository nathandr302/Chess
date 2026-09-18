package chess;

import java.util.Collection;


public abstract class BaseMovementRule implements MovementRule {
    protected void calculateMoves(ChessBoard board, ChessPosition pos, int rowInc, int colInc, Collection<ChessMove> moves, boolean allowDistance) {

        ChessPosition lastPosition = pos;
        ChessPosition newPosition = new ChessPosition(pos.getRow() + rowInc, pos.getColumn() + colInc);

        while (((lastPosition.getRow()) + rowInc <= 8 && (lastPosition.getColumn() + colInc) <= 8 &&
                (lastPosition.getRow() + rowInc) >= 1 && (lastPosition.getColumn() + colInc) >= 1)) {

            if (board.getPiece(newPosition) instanceof ChessPiece) {
                if (board.getPiece(pos).getTeamColor() == board.getPiece(newPosition).getTeamColor()) {
                    break;
                } else {
                    moves.add(new ChessMove(pos, newPosition, null));
                    break;
                }
            } else {
                moves.add(new ChessMove(pos, newPosition, null));
            }
            lastPosition = newPosition;
            newPosition = new ChessPosition(newPosition.getRow() + rowInc, newPosition.getColumn() + colInc);
            // if the piece that is passed in only has a limited range ie. the King, Pawn and the Knight
            if (!allowDistance) {
                return;
            }
        }
    }

    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition pos);
}
