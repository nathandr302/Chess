package chess;

import java.util.Collection;

import static chess.ChessPiece.PieceType.*;


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
                    calcPromoPiece(board, pos, newPosition, moves);
                    break;
                }
            }
            if (board.getPiece(pos).getPieceType() == PAWN && (newPosition.getRow() == 1 || newPosition.getRow() == 8)) {
                calcPromoPiece(board, pos, newPosition, moves);
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

    private void calcPromoPiece(ChessBoard board, ChessPosition currentPos, ChessPosition newPos, Collection<ChessMove> moves) {

        if (board.getPiece(currentPos).getPieceType() == PAWN && (newPos.getRow() == 1 || newPos.getRow() == 8)) {
            moves.add(new ChessMove(currentPos, newPos, QUEEN));
            moves.add(new ChessMove(currentPos, newPos, BISHOP));
            moves.add(new ChessMove(currentPos, newPos, KNIGHT));
            moves.add(new ChessMove(currentPos, newPos, ROOK));
        } else {
            moves.add(new ChessMove(currentPos, newPos, null));
        }

    }

    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition pos);
}
