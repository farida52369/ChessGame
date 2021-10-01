package sample.pieces;

import javafx.scene.image.ImageView;
import javafx.util.Pair;
import sample.MoveList;
import sample.Transition;

import java.util.ArrayList;
import java.util.List;

import static sample.Constants.MOVES_COUNT;
import static sample.Constants.OPTIMAL_SIZE;

public abstract class Piece {

    private ImageView pieceSprite;
    private final int rowPos, colPos;

    // To Check if it's White Or Black Piece
    private final Type type;

    // The first time a PAWN moves, it has the option of advancing two squares.
    // And for rook and king in castling case
    // The EnPassant move
    private boolean hasMoved, enPassant;

    public Piece(int rowPos, int colPos, Type type) {
        this.rowPos = rowPos;
        this.colPos = colPos;

        // In The Initialization, The Pawn never moved and no enPassant valid
        this.hasMoved = false;

        // Checking white Or black
        // To get the exact piece
        this.type = type;
    }

    // For the Pawn First Move!!
    public boolean getHasMoved() {
        return this.hasMoved;
    }

    public void setHasMoved(boolean check) {
        this.hasMoved = true;
    }

    public String getType() {
        return this.type.getType();
    }

    public double getOptimalSize() {
        return OPTIMAL_SIZE;
    }

    private boolean inRange(int x, int y) {
        return (x >= 0 && x < 8 && y >= 0 && y < 8);
    }

    private boolean hasPiece(Piece piece) {
        return piece != null;
    }

    // isValidPawnMove function to check if it's a valid pawn MOVE
    private boolean isValidPawnMove(Transition transition, Piece[][] board) {
        Piece pieceOld = board[transition.getOldX()][transition.getOldY()];
        Piece pieceNew = board[transition.getNewX()][transition.getNewY()];

        // It's not even a Pawn
        Piece pawn = new Pawn(transition.getOldX(), transition.getOldY(), pieceOld.type);
        if (!(pieceOld.getClass()).equals(pawn.getClass())) {
            return true;
        }

        // If Straight Move
        if (transition.getNewY() == 0) {

            // If Black is Positive 1 and if White is negative 1
            int colorMode = transition.getGapBetweenX() / Math.abs(transition.getGapBetweenX());

            // Two Squares allowed if First Move
            // Only One in any Move Later
            for (int i = 1; i <= Math.abs(colorMode); i++) {
                // If The Straight Move is Occupied! return false
                if (hasPiece(board[transition.getOldX() + (i * colorMode)][transition.getOldY()]))
                    return false;
            }
        } else {
            // If The Move is diagonal
            if (!hasPiece(pieceNew) || pieceOld.type == pieceNew.type)
                return false;

        }
        return true;
    }


    // isPossibleMove Is consider as the main Part in the move process!!
    public List<Pair<Integer, Integer>> allPossibleMoves(int xNow, int yNow, Piece[][] board) {

        // For The List that has all Possible Moves
        List<Pair<Integer, Integer>> moves = new ArrayList<>();

        // Checking if old and new Places are in range
        if (!inRange(xNow, yNow)) return moves;

        // The Exact Piece -- OLD One
        Piece piece = board[xNow][yNow];

        // Checking if OldPlace has a piece already or not!!
        if (!hasPiece(piece)) return moves;

        // Getting the possible Moves for the oldPiece
        MoveList[] moveLists = piece.getMoveList();


        // If it's Using moves more than the base Moves -- bishop queen rook
        int movesCount;
        int stretchMoveX;
        int stretchMoveY;

        // Main Loop
        for (MoveList move : moveLists) {

            // Has multiMoves or just a base move
            movesCount = (!piece.useBaseMoves()) ? MOVES_COUNT : 1;

            // if true, mean you could kill the Piece
            boolean ifHasCollided = false;

            for (int i = 1; i <= movesCount; i++) {
                if (ifHasCollided) break;

                // Updating The NowMove to See if it's Suit The Wanted Move
                stretchMoveX = move.getX() * i;
                stretchMoveY = move.getY() * i;

                // OldDirection + Stretching
                int x = xNow + stretchMoveX;
                int y = yNow + stretchMoveY;

                // TempPiece To check if stretching Place has a piece or not
                Piece tempPiece;

                if (inRange(x, y)) tempPiece = board[x][y];
                else break;

                // Can't Move in its Place
                if (x == xNow && y == yNow) break;

                // Checking if the place is occupied already but in enemyPiece
                if (hasPiece(tempPiece)) {
                    ifHasCollided = true;
                    if (tempPiece.type == piece.type) break;
                }

                if (!isValidPawnMove(new Transition(xNow, yNow, x, y), board)) continue;
                moves.add(new Pair<>(x, y));
            }
        }
        return moves;
    }

    // If it's Queen, Rook, or Bishop, then it's (false)
    public abstract boolean useBaseMoves();

    // To get the ImageView of the Piece
    public abstract ImageView getPieceSprite();

    // To get the MoveList of the piece
    public abstract MoveList[] getMoveList();
}
