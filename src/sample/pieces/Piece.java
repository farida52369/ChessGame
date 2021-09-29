package sample.pieces;

import javafx.scene.image.ImageView;
import sample.Game;
import sample.MoveList;
import sample.OldNewMove;

public abstract class Piece {

    private ImageView pieceSprite;
    private final int rowPos, colPos;

    // To Check if it's White Or Black Piece
    private final Type type;

    // For Width and Height
    private final double optimalWidth = 64;

    // The first time a PAWN moves, it has the option of advancing two squares.
    // And for rook and king in castling case
    // The EnPassant move
    private boolean hasMoved, enPassant;

    public Piece(int rowPos, int colPos, Type type) {
        this.rowPos = rowPos;
        this.colPos = colPos;

        // In The Initialization, The Pawn never moved and no enPassant valid
        this.hasMoved = false;
        this.enPassant = false;


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

    // For the Pawn EnPassant
    public boolean getEnPassant() {
        return this.enPassant;
    }

    public void setEnPassant(boolean check) {
        this.enPassant = check;
    }

    public String getType() {
        return this.type.getType();
    }

    public double getOptimalSize() {
        return this.optimalWidth;
    }

    private boolean inRange(int x, int y) {
        return (x >= 0 && x < 8 && y >= 0 && y < 8);
    }

    private boolean hasPiece(Piece piece) {
        return piece != null;
    }

    // isValidPawnMove function to check if it's a valid pawn MOVE
    private boolean isValidPawnMove(OldNewMove oldNewMove, Game game) {
        Piece pieceOld = game.getBoard()[oldNewMove.getOldX()][oldNewMove.getOldY()];
        Piece pieceNew = game.getBoard()[oldNewMove.getNewX()][oldNewMove.getNewY()];

        // It's not even a Pawn
        if (!pieceOld.equals(new Pawn(pieceOld.rowPos, pieceOld.colPos, pieceOld.type))) {
            return true;
        }

        // If Straight Move
        if (oldNewMove.getNewX() == 0) {

            // If Black is Positive 1 and if White is negative 1
            int colorMode = oldNewMove.getGapBetweenY() / Math.abs(oldNewMove.getGapBetweenY());

            // Two Squares allowed if First Move
            // Only One in any Move Later
            for (int i = 1; i <= Math.abs(colorMode); i++) {
                // If The Straight Move is Occupied! return false
                if (hasPiece(game.getBoard()[oldNewMove.getOldY()][oldNewMove.getOldY() + (i * colorMode)]))
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
    public boolean isPossibleMove(OldNewMove oldNewMove, Game game) {

        // Checking if old and new Places are in range
        if (!inRange(oldNewMove.getOldX(), oldNewMove.getOldY())) return false;
        if (!inRange(oldNewMove.getNewX(), oldNewMove.getNewY())) return false;

        // The Exact Piece -- OLD One
        Piece piece = game.getBoard()[oldNewMove.getOldX()][oldNewMove.getOldY()];

        // Checking if OldPlace has a piece already or not!!
        if (!hasPiece(piece)) return false;

        // Getting the possible Moves for the oldPiece
        MoveList[] moveLists = piece.getMoveList();
        boolean finallyPossibleMove = false;

        // If it's Using moves more than the base Moves -- bishop queen rook
        int movesCount;
        int stretchMoveX;
        int stretchMoveY;

        MainLoop:
        for (MoveList move : moveLists) {

            // System.out.println("MOVE BEFORE ANY THING: " + " X:" + move.getX() + " Y: " + move.getY());
            // Has multiMoves or just a base move
            movesCount = (!piece.useBaseMoves()) ? 8 : 1;

            // if true, mean you could kill the Piece
            boolean ifHasCollided = false;

            for (int i = 1; i <= movesCount; i++) {
                if (ifHasCollided) break;


                // Updating The NowMove to See if it's Suit The Wanted Move
                stretchMoveX = move.getX() * i;
                stretchMoveY = move.getY() * i;


                // OldDirection + Stretching
                int x = oldNewMove.getOldX() + stretchMoveX;
                int y = oldNewMove.getOldY() + stretchMoveY;

                Piece tempPiece;

                if (inRange(x, y)) tempPiece = game.getBoard()[x][y];
                else break;

                // Checking if the place is occupied already but in enemyPiece
                if (hasPiece(tempPiece)) {
                    ifHasCollided = true;
                    if (tempPiece.type == piece.type) break;
                }

                // If The Move In our Hand NOW is the same as The NewMove
                //
                if ((oldNewMove.getGapBetweenX() == stretchMoveX) &&
                        (oldNewMove.getGapBetweenY() == stretchMoveY)) {
                    finallyPossibleMove = true;

                    // What If the Piece was a pawn?
                    if (!isValidPawnMove(oldNewMove, game)) return false;

                    piece.setHasMoved(true);
                    break MainLoop;
                }
            }
        }
        return finallyPossibleMove;
    }


    public abstract boolean useBaseMoves();

    // To get the ImageView of the Piece
    public abstract ImageView getPieceSprite();

    // To get the MoveList of the piece
    public abstract MoveList[] getMoveList();
}
