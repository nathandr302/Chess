package chess;

import java.util.Collection;
import java.util.HashSet;


public class PawnMovementRule extends BaseMovementRule{
    @Override
    public Collection<ChessMove> moves(ChessBoard board, ChessPosition position){

        var row = position.getRow();
        var col = position.getColumn();
        var moves = new HashSet<ChessMove>();
        //diag moves
        if(board.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK && position.getRow() == 7){
            calculateMoves(board, position, -1, 0, moves, false);

        }else if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK && position.getRow() == 2){
            calculateMoves(board, position, 1, 0, moves, false);
        }





        return moves;
    }
}
