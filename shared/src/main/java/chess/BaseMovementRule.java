package chess;

import java.util.Collection;


public abstract class BaseMovementRule implements MovementRule {
    protected void calculateMoves(ChessBoard board, ChessPosition position, int rowInc, int colInc, Collection<ChessMove> moves, boolean allowDistance) {
        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        ChessPiece currentPiece = board.getPiece(position);

        while (true) {
            int newRow = currentRow + rowInc;
            int newCol = currentCol + colInc;

            if (newRow > 8 || newCol > 8 ||
                    newRow < 1 || newCol < 1) {
                break;
            }
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessPiece newPiece = board.getPiece(newPosition);
            if (newPiece != null) {
                if (currentPiece.getTeamColor() == newPiece.getTeamColor()) {
                    break;
                }
                moves.add(new ChessMove(position, newPosition, null));
                break;
            }

            moves.add(new ChessMove(position, newPosition, null));

            if (!allowDistance) {
                break;
            }
            currentCol = newCol;
            currentRow = newRow;
        }
    }

    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition pos);
}
