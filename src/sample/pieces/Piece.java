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


    // allPossibleMoves Is consider as the main Part in the move process!!
    // As it's return all the possible moves for a selected Piece
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

    // Checking if the king is under attack
    // (x, y) King's Position
    public List<Pair<Integer, Integer>> checkKing(int x, int y, Piece[][] board) {
        List<Pair<Integer, Integer>> checkingMoves = new ArrayList<>();

        // Checking The Four Directions (UP - DOWN - RIGHT - LEFT)
        // THE PIECES COULD MOVE THIS WAY ARE QUEEN AND ROOK
        // DOWN
        for (int i = 1; i <= MOVES_COUNT; i++) {
            if (inRange(x + i, y)) {
                Piece piece = board[x + i][y];
                if (piece != null) {
                    Piece queen = new Queen(piece.rowPos + i, piece.colPos, piece.type);
                    Piece rook = new Rook(piece.rowPos + i, piece.colPos, piece.type);
                    if ((board[x][y].getType()).equals(piece.getType()))
                        break;
                    if (((piece.getClass()).equals(queen.getClass())) || ((piece.getClass()).equals(rook.getClass()))) {
                        checkingMoves.add(new Pair<>(x + i, y));
                    }
                    break;
                }
            } else break;
        }

        // UP
        for (int i = 1; i <= MOVES_COUNT; i++) {
            if (inRange(x - i, y)) {
                Piece piece = board[x - i][y];
                if (piece != null) {
                    Piece queen = new Queen(piece.rowPos - i, piece.colPos, piece.type);
                    Piece rook = new Rook(piece.rowPos - i, piece.colPos, piece.type);
                    if ((board[x][y].getType()).equals(piece.getType()))
                        break;
                    if (((piece.getClass()).equals(queen.getClass())) || ((piece.getClass()).equals(rook.getClass()))) {
                        checkingMoves.add(new Pair<>(x - i, y));
                    }
                    break;
                }
            } else break;
        }

        // RIGHT
        for (int i = 1; i <= MOVES_COUNT; i++) {
            if (inRange(x, y + i)) {
                Piece piece = board[x][y + i];
                if (piece != null) {
                    Piece queen = new Queen(piece.rowPos, piece.colPos + i, piece.type);
                    Piece rook = new Rook(piece.rowPos, piece.colPos + i, piece.type);
                    if ((board[x][y].getType()).equals(piece.getType()))
                        break;
                    if (((piece.getClass()).equals(queen.getClass())) || ((piece.getClass()).equals(rook.getClass()))) {
                        checkingMoves.add(new Pair<>(x, y + i));
                    }
                    break;
                }
            } else break;
        }

        // LEFT
        for (int i = 1; i <= MOVES_COUNT; i++) {
            if (inRange(x, y - i)) {
                Piece piece = board[x][y - i];
                if (piece != null) {
                    Piece queen = new Queen(piece.rowPos, piece.colPos - i, piece.type);
                    Piece rook = new Rook(piece.rowPos, piece.colPos - i, piece.type);
                    if ((board[x][y].getType()).equals(piece.getType()))
                        break;
                    if (((piece.getClass()).equals(queen.getClass())) || ((piece.getClass()).equals(rook.getClass()))) {
                        checkingMoves.add(new Pair<>(x, y - i));
                    }
                    break;
                }
            } else break;
        }

        // Checking The Four Diagonals (RIGHT DOWN - RIGHT UP - LEFT DOWN - LEFT UP)
        // THE PIECES COULD MOVE THIS WAY ARE QUEEN AND BISHOP
        // RIGHT DOWN
        for (int i = 1; i <= MOVES_COUNT; i++) {
            if (inRange(x + i, y + i)) {
                Piece piece = board[x + i][y + i];
                if (piece != null) {
                    Piece queen = new Queen(piece.rowPos + i, piece.colPos + i, piece.type);
                    Piece bishop = new Bishop(piece.rowPos + i, piece.colPos + i, piece.type);
                    if ((board[x][y].getType()).equals(piece.getType()))
                        break;
                    if (((piece.getClass()).equals(queen.getClass())) || ((piece.getClass()).equals(bishop.getClass()))) {
                        checkingMoves.add(new Pair<>(x + i, y + i));
                    }
                    break;
                }
            } else break;
        }

        // RIGHT UP
        for (int i = 1; i <= MOVES_COUNT; i++) {
            if (inRange(x - i, y + i)) {
                Piece piece = board[x - i][y + i];
                if (piece != null) {
                    Piece queen = new Queen(piece.rowPos - i, piece.colPos + i, piece.type);
                    Piece bishop = new Bishop(piece.rowPos - i, piece.colPos + i, piece.type);
                    if ((board[x][y].getType()).equals(piece.getType()))
                        break;
                    if (((piece.getClass()).equals(queen.getClass())) || ((piece.getClass()).equals(bishop.getClass()))) {
                        checkingMoves.add(new Pair<>(x - i, y + i));
                    }
                    break;
                }
            } else break;
        }

        // LEFT DOWN
        for (int i = 1; i <= MOVES_COUNT; i++) {
            if (inRange(x + i, y - i)) {
                Piece piece = board[x + i][y - i];
                if (piece != null) {
                    Piece queen = new Queen(piece.rowPos + i, piece.colPos - i, piece.type);
                    Piece bishop = new Bishop(piece.rowPos + i, piece.colPos - i, piece.type);
                    if ((board[x][y].getType()).equals(piece.getType()))
                        break;
                    if (((piece.getClass()).equals(queen.getClass())) || ((piece.getClass()).equals(bishop.getClass()))) {
                        checkingMoves.add(new Pair<>(x + i, y - i));
                    }
                    break;
                }
            } else break;
        }

        // LEFT UP
        for (int i = 1; i <= MOVES_COUNT; i++) {
            if (inRange(x - i, y - i)) {
                Piece piece = board[x - i][y - i];
                if (piece != null) {
                    Piece queen = new Queen(piece.rowPos - i, piece.colPos - i, piece.type);
                    Piece bishop = new Bishop(piece.rowPos - i, piece.colPos - i, piece.type);
                    if ((board[x][y].getType()).equals(piece.getType()))
                        break;
                    if (((piece.getClass()).equals(queen.getClass())) || ((piece.getClass()).equals(bishop.getClass()))) {
                        checkingMoves.add(new Pair<>(x - i, y - i));
                    }
                    break;
                }
            } else break;
        }

        // CHECKING IF IT'S A KNIGHT OR NOT
        Piece knight = new Knight(0, 0, Type.BLACK);
        for (MoveList moveList : knight.getMoveList()) {
            int xKnight = x + moveList.getX(), yKnight = y + moveList.getY();
            if (inRange(xKnight, yKnight)) {
                Piece piece = board[xKnight][yKnight];
                if (piece != null) {
                    if ((board[x][y].getType()).equals(piece.getType()))
                        continue;
                    if ((piece.getClass()).equals(knight.getClass()))
                        checkingMoves.add(new Pair<>(xKnight, yKnight));
                }
            }
        }

        // CHECKING IF IT'S A PAWN
        int colorMode = (board[x][y].getType().equals("white")) ? -1 : 1;

        // Case 1
        // BAD PAWN VARIABLE BUT WHAT IN THE HAND :((
        Piece pawn = new Pawn(0, 0, Type.BLACK);
        int xPawn1 = colorMode + x, yPawn1 = colorMode + y;
        if (inRange(xPawn1, yPawn1)) {
            Piece piece = board[xPawn1][yPawn1];
            if (piece != null && !(piece.getType()).equals(board[x][y].getType())) {
                if ((piece.getClass()).equals(pawn.getClass()))
                    checkingMoves.add(new Pair<>(xPawn1, yPawn1));
            }
        }

        // Case 2
        int xPawn2 = x + colorMode, yPawn2 = -1 * colorMode + y;
        if (inRange(xPawn2, yPawn2)) {
            Piece piece = board[xPawn2][yPawn2];
            if (piece != null && !(piece.getType()).equals(board[x][y].getType())) {
                if ((piece.getClass()).equals(pawn.getClass()))
                    checkingMoves.add(new Pair<>(xPawn2, yPawn2));
            }
        }

        return checkingMoves;
    }

    // If it's Queen, Rook, or Bishop, then it's (false)
    public abstract boolean useBaseMoves();

    // To get the ImageView of the Piece
    public abstract ImageView getPieceSprite();

    // To get the MoveList of the piece
    public abstract MoveList[] getMoveList();
}
