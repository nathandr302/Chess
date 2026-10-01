package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;


public class PawnMovementRule extends BaseMovementRule {
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();

        int startRow;
        int direction;
        int promoRow;

        ChessPiece piece = board.getPiece(position);

        if (piece.getTeamColor() == WHITE) {
            startRow = 2;
            direction = 1;
            promoRow = 8;
        } else {
            startRow = 7;
            direction = -1;
            promoRow = 1;
        }
        int currentRow = position.getRow();
        int currentCol = position.getColumn();


        ChessPosition newPosition = new ChessPosition(currentRow + direction, currentCol);
        ChessPiece newPiece = board.getPiece(newPosition);
        if (newPiece == null) {
            if (currentRow == startRow) {
                ChessPosition doubleFront = new ChessPosition(currentRow + (2 * direction), currentCol);
                var doublePiece = board.getPiece(doubleFront);
                if (doublePiece == null) {
                    moves.add(new ChessMove(position, doubleFront, null));
                }
            }
            pawnHelper(position, newPosition, moves, promoRow);
        }
        // diag col - 1
        if (currentCol - 1 >= 1) {
            int newRow = currentRow + direction;
            int newcol = currentCol - 1;
            ChessPosition diag = new ChessPosition(newRow, newcol);
            ChessPiece diagPiece = board.getPiece(diag);

            if (diagPiece != null) {
                if (board.getPiece(diag).getTeamColor() != piece.getTeamColor()) {
                    pawnHelper(position, diag, moves, promoRow);
                }
            }
        }
        // diag col + 1
        if (currentCol + 1 <= 8) {
            int newRow = currentRow + direction;
            int newcol = currentCol + 1;
            ChessPosition diag = new ChessPosition(newRow, newcol);
            ChessPiece diagPiece = board.getPiece(diag);

            if (diagPiece != null) {
                if (board.getPiece(diag).getTeamColor() != piece.getTeamColor()) {
                    pawnHelper(position, diag, moves, promoRow);
                }
            }
        }
        return moves;
    }

    public void pawnHelper(ChessPosition startPos, ChessPosition endPos, Collection<ChessMove> moves, int promoRow) {
        int endCol = endPos.getRow();
        if (endCol == promoRow) {
            moves.add(new ChessMove(startPos, endPos, QUEEN));
            moves.add(new ChessMove(startPos, endPos, KNIGHT));
            moves.add(new ChessMove(startPos, endPos, BISHOP));
            moves.add(new ChessMove(startPos, endPos, ROOK));
        } else {
            moves.add(new ChessMove(startPos, endPos, null));
        }

    }
}
