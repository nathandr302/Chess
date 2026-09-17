package chess;

import java.util.Collection;
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
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // ChessPiece piece = board.getPiece(myPosition);
//
        // List<ChessMove> validMoves = new java.util.ArrayList<>(List.of());
//        if (piece.getPieceType() == PieceType.BISHOP){
//
//            for (int i = myPosition.getRow()+1, j = myPosition.getColumn()+1; i  <= 8 && j<=8; i++, j++){
//                validMoves.add(new ChessMove(myPosition, new ChessPosition(i,j), null));
//            }
//            for (int i = myPosition.getRow()+1, j = myPosition.getColumn()-1; i  <= 8 && j>=1; i++, j--){
//                validMoves.add(new ChessMove(myPosition, new ChessPosition(i,j), null));
//            }
//            for (int i = myPosition.getRow()-1, j = myPosition.getColumn()+1; i  >= 1 && j<=8; i--, j++){
//                validMoves.add(new ChessMove(myPosition, new ChessPosition(i,j), null));
//            }
//            for (int i = myPosition.getRow()-1, j = myPosition.getColumn()-1; i  >= 1 && j>= 1; i--, j--){
//                validMoves.add(new ChessMove(myPosition, new ChessPosition(i,j), null));
//            }
//        } else if (piece.getPieceType() == PieceType.ROOK) {
//            for (int i = myPosition.getRow() + 1; i <= 8; i++) {
//                validMoves.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
//            }
//            for (int i = myPosition.getRow() - 1; i >= 1; i--) {
//                validMoves.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
//            }
//            for (int i = myPosition.getColumn() + 1; i <= 8; i++) {
//                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));
//            }
//            for (int i = myPosition.getColumn() - 1; i >= 1; i--) {
//                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));
//            }
//        } else if (piece.getPieceType() == PieceType.QUEEN) {
//            //vert and horz moves
//            for (int i = myPosition.getRow() + 1; i <= 8; i++) {
//                validMoves.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
//            }
//            for (int i = myPosition.getRow() - 1; i >= 1; i--) {
//                validMoves.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
//            }
//            for (int i = myPosition.getColumn() + 1; i <= 8; i++) {
//                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));
//            }
//            for (int i = myPosition.getColumn() - 1; i >= 1; i--) {
//                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));
//            }
//            for (int i = myPosition.getRow()+1, j = myPosition.getColumn()+1; i  <= 8 && j<=8; i++, j++){
//                validMoves.add(new ChessMove(myPosition, new ChessPosition(i,j), null));
//            }
//            for (int i = myPosition.getRow()+1, j = myPosition.getColumn()-1; i  <= 8 && j>=1; i++, j--){
//                validMoves.add(new ChessMove(myPosition, new ChessPosition(i,j), null));
//            }
//            for (int i = myPosition.getRow()-1, j = myPosition.getColumn()+1; i  >= 1 && j<=8; i--, j++){
//                validMoves.add(new ChessMove(myPosition, new ChessPosition(i,j), null));
//            }
//            for (int i = myPosition.getRow()-1, j = myPosition.getColumn()-1; i  >= 1 && j>= 1; i--, j--) {
//                validMoves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
//            }
//        }
//
//        return validMoves;
        Rules rules = new Rules();
       return rules.pieceRule(this.type).move(board, myPosition);

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
