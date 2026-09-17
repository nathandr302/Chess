package chess;

import java.util.Collection;


public  abstract class BaseMovementRule implements MovementRule{
    protected void calculateMoves (ChessBoard board, ChessPosition pos, int rowInc, int colInc, Collection<ChessMove> moves, boolean allowDistance){
        ChessPosition newPosition = new ChessPosition(pos.getRow()+rowInc, pos.getColumn()+colInc);

        while ((newPosition.getRow()+rowInc <= 9 && newPosition.getColumn()+colInc <= 9 &&
                newPosition.getRow()+rowInc >= 0 && newPosition.getColumn()+colInc >= 0)){

            if(board.getPiece(newPosition) instanceof ChessPiece){
                if (board.getPiece(pos).getTeamColor() == board.getPiece(newPosition).getTeamColor()){
                    break;
                }
                else{
                    moves.add(new ChessMove(pos, newPosition, null));
                    break;
                }
            }
            else{
                moves.add(new ChessMove(pos, newPosition, null));
            }
            newPosition = new ChessPosition(newPosition.getRow()+rowInc, newPosition.getColumn()+colInc);
        }

    }
    public abstract Collection<ChessMove> moves(ChessBoard board, ChessPosition pos);
}
