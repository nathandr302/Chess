package chess;

import java.util.Collection;
import java.util.List;

public class PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP){
            return List.of(new ChessMove(new ChessPosition(2,5), new ChessPosition(2,5), null));
        }
        return List.of();
    }
}

