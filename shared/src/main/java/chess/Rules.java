package chess;

import java.util.HashMap;

import static chess.ChessPiece.PieceType.*;

public class Rules {
    private final HashMap<ChessPiece.PieceType, MovementRule> rules = new HashMap<>();

    public Rules(){
        rules.put(BISHOP, new BishopMovementRule());
        rules.put(KING, new KingMovementRule());
        rules.put(KNIGHT, new KnightMovementRule());
        rules.put(PAWN, new PawnMovementRule());
        rules.put(QUEEN, new QueenMovementRule());
        rules.put(ROOK, new RookMovementRule());
    }

    public MovementRule pieceRule(ChessPiece.PieceType type){
        return rules.get(type);
    }
}
