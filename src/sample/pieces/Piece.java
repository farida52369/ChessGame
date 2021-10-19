package sample.pieces;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import sample.MoveList;
import sample.Transition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static sample.Constants.*;

public abstract class Piece {

    // To update the position of each piece
    private int rowPos, colPos;

    // To Check if it's White Or Black Piece
    private final Type type;

    // Checking of hasMoved before or not --
    // In case it's a pawn to handle the moves
    //
    private boolean hasMoved;


    private Alert alert;

    public Piece(int rowPos, int colPos, Type type) {
        this.rowPos = rowPos;
        this.colPos = colPos;

        // Checking white Or black
        // To get the exact piece
        this.type = type;

        // Setting hasMoved to false as a start.
        this.hasMoved = false;
    }

    // Get if the piece has moved before.
    public boolean getHasMoved() {
        return !(this.hasMoved);
    }

    // Set hasMoved to true, when I move.
    public void setHasMoved() {
        this.hasMoved = true;
    }

    // Getters for rowPosition
    public int getRowPos() {
        return this.rowPos;
    }

    // Getters for colPosition
    public int getColPos() {
        return this.colPos;
    }

    // Setters for rowPosition and colPosition
    // To Update the position of the Piece :)
    public void setRowColPos(int x, int y) {
        this.rowPos = x;
        this.colPos = y;
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
        if (transition.getGapBetweenY() == 0) {

            // The Pawn can't capture a piece in the same direction
            if (pieceNew != null)
                return false;

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
            return hasPiece(pieceNew) && pieceOld.type != pieceNew.type;
        }
        return true;
    }


    // allPossibleMoves Is consider as the main Part in the move process!!
    // As it's return all the possible moves for a selected Piece
    public List<Pair<Integer, Integer>> allPossibleMoves(Piece[][] board) {

        int xNow = this.rowPos, yNow = this.colPos;
        // For The List that has all Possible Moves
        List<Pair<Integer, Integer>> moves = new ArrayList<>();

        // Checking if old Place are in range
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
    // (x -- this.rowPos, y -- this.colPos) King's Position
    public List<Pair<Integer, Integer>> checkKing(Piece[][] board) {
        int x = this.rowPos, y = this.colPos;
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

    // CheckMate Function To check if the king could move or had lost the game Already!!
    public HashSet<Pair<Integer, Integer>> checkMate(Piece[][] board) {

        // The King's (X, Y)
        int xKing = this.rowPos, yKing = this.colPos;

        // movesToUncheckTheKing HashMap has all the places that could be chosen to prevent CheckMate
        HashSet<Pair<Integer, Integer>> movesToUncheckTheKing = new HashSet<>();

        // Save King's move, So the king has to move didn't lose king's place
        int tempXKing = xKing, tempYKing = yKing;

        // Loop all the board
        // For the pieces the same type as the king
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (board[i][j] != null &&
                        board[i][j].getType().equals(board[xKing][yKing].getType())) {

                    // For every piece possible moves
                    List<Pair<Integer, Integer>> sameTypePieceMoves = board[i][j].allPossibleMoves(board);

                    for (Pair<Integer, Integer> k : sameTypePieceMoves) {

                        if (i == xKing && j == yKing) {
                            xKing = k.getKey();
                            yKing = k.getValue();
                        }

                        assert board[xKing][yKing] != null;

                        // Save The piece if found
                        Piece piece = board[k.getKey()][k.getValue()];

                        // Move The (i, j) to the possible move to check in this case
                        // if still CHECK
                        moveFunction(new Transition(i, j, k.getKey(), k.getValue()), board);

                        // Check if the move cause uncheck King
                        List<Pair<Integer, Integer>> movesIfKingChecked = board[xKing][yKing].checkKing(board);

                        // Move the piece to return the same
                        moveFunction(new Transition(k.getKey(), k.getValue(), i, j), board);

                        // if the piece wasn't null, return its value
                        board[k.getKey()][k.getValue()] = piece;

                        if (movesIfKingChecked.size() == 0) {
                            movesToUncheckTheKing.add(new Pair<>(k.getKey(), k.getValue()));
                        }

                        xKing = tempXKing;
                        yKing = tempYKing;
                    }
                }
            }
        }
        return movesToUncheckTheKing;
    }

    public boolean staleMate(Piece[][] board) {

        // The King's (X, Y)
        int xKing = this.rowPos, yKing = this.colPos;

        // Save King's move, So the king has to move didn't lose king's place
        int tempXKing = xKing, tempYKing = yKing;

        // Loop all the board
        // For the pieces the same type as the king
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (board[i][j] != null &&
                        board[i][j].getType().equals(board[xKing][yKing].getType())) {

                    // For every piece possible moves
                    List<Pair<Integer, Integer>> sameTypePieceMoves = board[i][j].allPossibleMoves(board);

                    for (Pair<Integer, Integer> k : sameTypePieceMoves) {

                        if (i == xKing && j == yKing) {
                            xKing = k.getKey();
                            yKing = k.getValue();
                        }

                        assert board[xKing][yKing] != null;

                        // Save The piece if found
                        Piece piece = board[k.getKey()][k.getValue()];

                        // Move The (i, j) to the possible move to check in this case
                        // if still CHECK
                        moveFunction(new Transition(i, j, k.getKey(), k.getValue()), board);

                        // Check if the move cause uncheck King
                        List<Pair<Integer, Integer>> movesIfKingChecked = board[xKing][yKing].checkKing(board);

                        // Move the piece to return the same
                        moveFunction(new Transition(k.getKey(), k.getValue(), i, j), board);

                        // if the piece wasn't null, return it's value
                        board[k.getKey()][k.getValue()] = piece;


                        if (movesIfKingChecked.size() == 0) {
                            return false;
                        }

                        xKing = tempXKing;
                        yKing = tempYKing;
                    }
                }
            }
        }
        return true;
    }


    // Choose Promotion Piece using Alert System and buttons
    // (X, Y) for the new position the promoted piece is going to __
    public Piece choosePromotePiece(int x, int y) {
        Piece promotedPiece = null;

        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Promotion");
        alert.setHeaderText("You can promote your pawn into another piece");
        alert.setContentText("Choose one of the following pieces: ");

        ButtonType buttonRook = new ButtonType("Rook");
        ButtonType buttonKnight = new ButtonType("Knight");
        ButtonType buttonBishop = new ButtonType("Bishop");
        ButtonType buttonQueen = new ButtonType("Queen");

        alert.getButtonTypes().setAll(buttonRook, buttonKnight, buttonBishop, buttonQueen);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonRook) {
            promotedPiece = new Rook(x, y, this.type);
        } else if (result.get() == buttonKnight) {
            promotedPiece = new Knight(x, y, this.type);
        } else if (result.get() == buttonBishop) {
            promotedPiece = new Bishop(x, y, this.type);
        } else if (result.get() == buttonQueen) {
            promotedPiece = new Queen(x, y, this.type);
        }

        return promotedPiece;
    }


    // Boolean Value Returns if the King under Attack
    // (X, Y) are king's RowPos, ColPOs__
    public boolean isKingUnderAttack(Piece[][] board) {
        List<Pair<Integer, Integer>> placesMakesKingUnderAttack = board[this.rowPos][this.colPos].checkKing(board);
        return (placesMakesKingUnderAttack.size() != 0);
    }

    // Boolean Value Returns if the specified King is checkMate
    // then The player lost the game
    public boolean isCheckMate(Piece[][] board) {
        HashSet<Pair<Integer, Integer>> checkMateMoves = board[this.rowPos][this.colPos].checkMate(board);
        return (checkMateMoves.size() == 0);
    }


    public void moveFunction(Transition transition, Piece[][] board) {
        int xOld = transition.getOldX(), yOld = transition.getOldY();
        int xNew = transition.getNewX(), yNew = transition.getNewY();

        board[xNew][yNew] = board[xOld][yOld];
        board[xOld][yOld] = null;
        board[xNew][yNew].setRowColPos(xNew, yNew);
    }


    // To get the image -- break the program to small pieces as possible
    public abstract Image getImage();

    // If it's Queen, Rook, or Bishop, then it's (false)
    public abstract boolean useBaseMoves();

    // To Set The Image View
    public abstract void setPieceSprite(Image image);

    // To get the ImageView of the Piece
    public abstract ImageView getPieceSprite();

    // To get the MoveList of the piece
    public abstract MoveList[] getMoveList();
}
