package chess;

import java.util.Collection;


public  abstract class BaseMovementRule implements MovementRule{
    protected void calculateMoves (ChessBoard board, ChessPosition pos, int rowInc, int colInc, Collection<ChessMove> moves, boolean allowDistance){
        if(pos.getRow()+rowInc > 8 && pos.getColumn()+colInc > 8 &&
                pos.getRow()+rowInc < 1 && pos.getColumn()+colInc < 1){
            return;
        }

        ChessPosition newPosition = new ChessPosition(pos.getRow()+rowInc, pos.getColumn()+colInc);

        if(board.getPiece(newPosition) instanceof ChessPiece){
            if (board.getPiece(pos).getTeamColor() == board.getPiece(newPosition).getTeamColor()){
                return;
            }
            else{
                moves.add(new ChessMove(pos, newPosition, null));
            }
        }
        else{
            moves.add(new ChessMove(pos, newPosition, null));
        }
    }
    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition pos);
}
