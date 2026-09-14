package chess;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor()
    {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType()
    {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        PieceMovesCalculator(type, myPosition);



        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == PieceType.BISHOP){
            return List.of(new ChessMove(new ChessPosition(5,4), new ChessPosition(2,5), null));
        }
        return List.of();


        return List.of(PieceMovesCalculator);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}

/**
 * calculates all the possible move that a single piece can have
 * @param myPosition current posision of the piece
 * @param type the piece type to dictate the move method
 * @return a collection of  all posiable moves
 */
public Collection<ChessMove> PieceMovesCalculator(ChessPosition myPosition, ChessPiece.PieceType type){
    Collection<ChessMove> listValidMoves = List.of();

    switch(type){
        case KING: return KingMovesCalculator(myPosition);
        case QUEEN: return QueenMovesCalculator(myPosition);
        case BISHOP: return BishopMovesCalculator(myPosition);
        case KNIGHT: return KnightMovesCalculator(myPosition);
        case ROOK: return RookMovesCalculator(myPosition);
        case PAWN: return PawnMovesCalculator(myPosition);
    }
    return listValidMoves;
}

public Collection<ChessMove> KingMovesCalculator(ChessPosition myPosition){
    Collection<ChessMove> listValidMoves = List.of();

    return listValidMoves;
}
public Collection<ChessMove> QueenMovesCalculator(ChessPosition myPosition){
    Collection<ChessMove> listValidMoves = List.of();

    return listValidMoves;
}
public Collection<ChessMove> KnightMovesCalculator(ChessPosition myPosition){

    return List.of();
}
public Collection<ChessMove> PawnMovesCalculator(ChessPosition myPosition){
    Collection<ChessMove> listValidMoves = List.of();

    return listValidMoves;
}
public Collection<ChessMove> BishopMovesCalculator(ChessPosition myPosition){
    Collection<ChessMove> listValidMoves = List.of();

    return listValidMoves;
}
public Collection<ChessMove> RookMovesCalculator(ChessPosition myPosition){
    Collection<ChessMove> listValidMoves = List.of();

    return listValidMoves;
}