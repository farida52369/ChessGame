package sample;

import javafx.scene.control.Alert;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;
import sample.pieces.King;
import sample.pieces.Pawn;
import sample.pieces.Piece;
import sample.pieces.Type;

import java.util.*;

import static sample.Constants.*;

public class MoveBoardUI {

    // To Start The game
    private String currentType;

    // Kings variables
    private int whiteKingX, whiteKingY;
    private int blackKingX, blackKingY;

    // Save the moves and the click Of last Click
    Stack<List<Pair<Integer, Integer>>> saveMovesOfLastClick = new Stack<>();
    Stack<Pair<Integer, Integer>> lastClick = new Stack<>();

    // To check Moving :_)
    Piece pawn = new Pawn(0, 0, Type.BLACK);
    Piece king = new King(0, 0, Type.BLACK);

    // Boolean __
    private boolean flag;

    public MoveBoardUI() {
        // For The White Color to start the game
        this.currentType = "white";

        // Set the Kings values
        this.whiteKingX = 7;
        this.whiteKingY = 4;

        this.blackKingX = 0;
        this.blackKingY = 4;
    }


    public boolean validEvent(double y, double x) {
        return (x >= (LABEL_SIZE + PADDING_VALUE) &&
                x <= (OPTIMAL_SIZE * 8 + LABEL_SIZE + PADDING_VALUE) &&
                y >= (LABEL_SIZE + PADDING_VALUE) &&
                y <= (OPTIMAL_SIZE * 8 + LABEL_SIZE + PADDING_VALUE)
        );
    }

    public void highLightBackGround(int x, int y, Background background, GridPane gridPane) {
        StackPane field = new StackPane();
        field.setBackground(background);
        gridPane.add(field, y + 1, x + 1);
    }

    public void highLight(List<Pair<Integer, Integer>> moves, Piece[][] board, GridPane gridPane) {
        for (Pair<Integer, Integer> i : moves) {
            int iX = i.getKey(), iY = i.getValue();
            if (board[iX][iY] == null) {
                highLightBackGround(iX, iY, ((iX + iY) & 1) == 0 ? WHITE_BLUE : BLACK_BLUE, gridPane);
            } else {
                // Saving the piece before any work
                Piece piece = board[iX][iY];

                // remove the NODE from the gridPane
                gridPane.getChildren().remove(piece.getPieceSprite());
                highLightBackGround(iX, iY, ((iX + iY) & 1) == 0 ? WHITE_RED : BLACK_RED, gridPane);

                // add The NODE again
                gridPane.add(piece.getPieceSprite(), iY + 1, iX + 1);
            }
        }
    }

    public void unHighLight(List<Pair<Integer, Integer>> movesToUnHighlight, Piece[][] board, GridPane gridPane) {
        for (Pair<Integer, Integer> i : movesToUnHighlight) {
            int iX = i.getKey(), iY = i.getValue();
            if (board[iX][iY] == null) {
                highLightBackGround(iX, iY, ((iX + iY) & 1) == 0 ? WHITE : GREY, gridPane);
            } else {
                // Saving the piece before any work
                Piece piece = board[iX][iY];
                // remove the NODE from the gridPane
                gridPane.getChildren().remove(piece.getPieceSprite());

                highLightBackGround(iX, iY, ((iX + iY) & 1) == 0 ? WHITE : GREY, gridPane);
                // adding the NODE to the updated place
                gridPane.add(piece.getPieceSprite(), iY + 1, iX + 1);
            }
        }
    }

    public void gameLogic(Transition transition, Piece[][] board, GridPane gridPane) {
        int lastClickX = transition.getOldX(), lastClickY = transition.getOldY();
        int x = transition.getNewX(), y = transition.getNewY();

        Piece piece = board[lastClickX][lastClickY];
        if (piece.getClass().equals(pawn.getClass()) &&
                ((piece.getType().equals("white") && x == 0) || (piece.getType().equals("black") && x == 7))) {
            if (board[x][y] == null)
                gridPane.getChildren().remove(board[lastClickX][lastClickY].getPieceSprite());
            else {
                gridPane.getChildren().removeAll(board[lastClickX][lastClickY].getPieceSprite(),
                        board[x][y].getPieceSprite());
            }
            board[x][y] = board[lastClickX][lastClickY].choosePromotePiece(x, y);
            board[lastClickX][lastClickY] = null;
            gridPane.add(board[x][y].getPieceSprite(), y + 1, x + 1);
        } else {

            if (board[x][y] == null)
                gridPane.getChildren().remove(board[lastClickX][lastClickY].getPieceSprite());
            else {
                gridPane.getChildren().removeAll(board[lastClickX][lastClickY].getPieceSprite(),
                        board[x][y].getPieceSprite());
            }
            gridPane.add(piece.getPieceSprite(), y + 1, x + 1);
            board[lastClickX][lastClickY].moveFunction(new Transition(lastClickX, lastClickY, x, y), board);
            if (piece.getClass().equals(pawn.getClass())) {
                board[x][y].setHasMoved();
            }
            if (piece.getClass().equals(king.getClass())) {
                if (piece.getType().equals("white")) {
                    highLightBackGround(whiteKingX, whiteKingY, ((whiteKingX + whiteKingY) & 1) == 0 ? WHITE : GREY, gridPane);
                    whiteKingX = x;
                    whiteKingY = y;
                } else {
                    highLightBackGround(blackKingX, blackKingY, ((blackKingX + blackKingY) & 1) == 0 ? WHITE : GREY, gridPane);
                    blackKingX = x;
                    blackKingY = y;

                }
            }
        }
        currentType = (currentType.equals("black")) ? "white" : "black";
        saveMovesOfLastClick = new Stack<>();
        lastClick = new Stack<>();
    }

    // Function to Paint The King That Under Attack
    public void paintKingUnderAttack(int x, int y, Piece[][] board, GridPane gridPane) {

        highLightBackGround(x, y, ((x + y) & 1) == 0 ? WHITE_RED : BLACK_RED, gridPane);
        Piece piece = board[x][y];
        gridPane.getChildren().remove(piece.getPieceSprite());
        gridPane.add(piece.getPieceSprite(), y + 1, x + 1);

        if (board[x][y].isCheckMate(board)) {
            System.out.printf("You Lost The Game %s%n", currentType);
            checkMateAlert();
            gridPane.setDisable(true);
        }
    }


    // Alert for the stalemate __ TIE
    public void staleMateAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("StaleMate");
        alert.setHeaderText("That's a TIE");
        alert.setContentText("The Game is Over in a TIE");
        alert.show();
    }

    // Alert for the CheckMate
    public void checkMateAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("CheckMate");
        alert.setHeaderText("Game Over");
        alert.setContentText(String.format("Congratulations, %s player!", (currentType.equals("white") ? "Black" : "White")));
        alert.show();
    }


    public void selectPiece(GridPane gridPane, Piece[][] board) {

        gridPane.setOnMouseClicked(mouseEvent -> {

            // Getting x and y MouseEvents
            double yEvent = mouseEvent.getY(), xEvent = mouseEvent.getX();

            // yEvent, xEvent check if they are valid
            // as they should be in the board
            if (validEvent(yEvent, xEvent)) {
                flag = false;
                boolean checkMate = false;

                // x, y are board dimensions
                int x = (int) ((yEvent - 30) / 64), y = (int) ((xEvent - 30) / 64);

                // Moves for the CURRENT CLICK
                if (board[x][y] != null) {

                    if (board[x][y].getType().equals(currentType)) {

                        List<Pair<Integer, Integer>> moves = board[x][y].allPossibleMoves(board);

                        // If there's moves To prevent checkMate
                        HashSet<Pair<Integer, Integer>> checkMateMoves = new HashSet<>();

                        // Check If There was a move before
                        if (!saveMovesOfLastClick.empty() && !lastClick.empty()) {

                            // Moves for the last CLICK
                            List<Pair<Integer, Integer>> movesToUnHighlight = saveMovesOfLastClick.pop();
                            Pair<Integer, Integer> lastClickVar = lastClick.pop();

                            unHighLight(movesToUnHighlight, board, gridPane);

                            int lastClickX = lastClickVar.getKey(), lastClickY = lastClickVar.getValue();

                            for (Pair<Integer, Integer> i : movesToUnHighlight) {
                                if (x == i.getKey() && y == i.getValue()) {
                                    flag = true;
                                    break;
                                }
                            }

                            if (flag) {

                                if (currentType.equals("white") && board[whiteKingX][whiteKingY].isKingUnderAttack(board)) {
                                    checkMateMoves = board[whiteKingX][whiteKingY].checkMate(board);
                                } else if (currentType.equals("black") && board[blackKingX][blackKingY].isKingUnderAttack(board)) {
                                    checkMateMoves = board[blackKingX][blackKingY].checkMate(board);
                                }
                                if (checkMateMoves.size() != 0) {
                                    for (Pair<Integer, Integer> p : checkMateMoves)
                                        if (x == p.getKey() && y == p.getValue())
                                            checkMate = true;
                                    if (checkMate)
                                        gameLogic(new Transition(lastClickX, lastClickY, x, y), board, gridPane);
                                } else {
                                    gameLogic(new Transition(lastClickX, lastClickY, x, y), board, gridPane);
                                }
                            }
                        }

                        if (currentType.equals("white") && board[whiteKingX][whiteKingY].isKingUnderAttack(board)) {
                            boolean check = false;
                            List<Pair<Integer, Integer>> temp = moves;
                            moves = new ArrayList<>();
                            checkMateMoves = board[whiteKingX][whiteKingY].checkMate(board);
                            for (Pair<Integer, Integer> p : temp) {
                                for (Pair<Integer, Integer> checkM : checkMateMoves) {
                                    if (Objects.equals(p.getKey(), checkM.getKey()) &&
                                            Objects.equals(checkM.getValue(), p.getValue())) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (check) {
                                    moves.add(new Pair<>(p.getKey(), p.getValue()));
                                    check = false;
                                }
                            }
                        }

                        if (!flag) {
                            highLight(moves, board, gridPane);
                            saveMovesOfLastClick.push(moves);
                            lastClick.push(new Pair<>(x, y));
                        }
                    } else if (!board[x][y].getType().equals(currentType) && !saveMovesOfLastClick.empty()) {

                        List<Pair<Integer, Integer>> lastMoves = saveMovesOfLastClick.pop();
                        Pair<Integer, Integer> lastClickXY = lastClick.pop();

                        int lastClickX = lastClickXY.getKey(), lastClickY = lastClickXY.getValue();

                        for (Pair<Integer, Integer> i : lastMoves) {
                            if (x == i.getKey() && y == i.getValue()) {
                                flag = true;
                                break;
                            }
                        }

                        if (flag) {
                            HashSet<Pair<Integer, Integer>> checkMateMoves = new HashSet<>();
                            if (currentType.equals("white") && board[whiteKingX][whiteKingY].isKingUnderAttack(board)) {
                                checkMateMoves = board[whiteKingX][whiteKingY].checkMate(board);
                            } else if (currentType.equals("black") && board[blackKingX][blackKingY].isKingUnderAttack(board)) {
                                checkMateMoves = board[blackKingX][blackKingY].checkMate(board);
                            }
                            if (checkMateMoves.size() != 0) {
                                for (Pair<Integer, Integer> p : checkMateMoves)
                                    if (x == p.getKey() && y == p.getValue())
                                        checkMate = true;
                                if (checkMate) gameLogic(new Transition(lastClickX, lastClickY, x, y), board, gridPane);
                            } else {
                                gameLogic(new Transition(lastClickX, lastClickY, x, y), board, gridPane);
                            }
                        }
                        unHighLight(lastMoves, board, gridPane);
                    }
                } else if (board[x][y] == null && !saveMovesOfLastClick.empty()) {

                    List<Pair<Integer, Integer>> lastMoves = saveMovesOfLastClick.pop();
                    Pair<Integer, Integer> lastClickXY = lastClick.pop();

                    int lastClickX = lastClickXY.getKey(), lastClickY = lastClickXY.getValue();

                    for (Pair<Integer, Integer> i : lastMoves) {
                        if (x == i.getKey() && y == i.getValue()) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        HashSet<Pair<Integer, Integer>> checkMateMoves = new HashSet<>();
                        if (currentType.equals("white") && board[whiteKingX][whiteKingY].isKingUnderAttack(board)) {
                            checkMateMoves = board[whiteKingX][whiteKingY].checkMate(board);
                        } else if (currentType.equals("black") && board[blackKingX][blackKingY].isKingUnderAttack(board)) {
                            checkMateMoves = board[blackKingX][blackKingY].checkMate(board);
                        }
                        if (checkMateMoves.size() != 0) {
                            for (Pair<Integer, Integer> p : checkMateMoves)
                                if (x == p.getKey() && y == p.getValue())
                                    checkMate = true;
                            if (checkMate) gameLogic(new Transition(lastClickX, lastClickY, x, y), board, gridPane);
                        } else {
                            gameLogic(new Transition(lastClickX, lastClickY, x, y), board, gridPane);
                        }
                    }
                    unHighLight(lastMoves, board, gridPane);
                }
            }

            // Boolean values for white and black kings if they are under attack
            boolean w_KingUnderAttack = board[whiteKingX][whiteKingY].isKingUnderAttack(board);
            boolean b_KingUnderAttack = board[blackKingX][blackKingY].isKingUnderAttack(board);

            // Painting the king's Piece under check
            if (currentType.equals("white")) {
                if (w_KingUnderAttack)
                    paintKingUnderAttack(whiteKingX, whiteKingY, board, gridPane);

                if (!w_KingUnderAttack && board[whiteKingX][whiteKingY].staleMate(board)) {
                    System.out.println("That's a TIE!!");
                    staleMateAlert();
                    gridPane.setDisable(true);
                }

                if (!b_KingUnderAttack) {
                    highLightBackGround(blackKingX, blackKingY, ((blackKingX + blackKingY) & 1) == 0 ? WHITE : GREY, gridPane);
                    Piece piece2 = board[blackKingX][blackKingY];
                    gridPane.getChildren().remove(piece2.getPieceSprite());
                    gridPane.add(piece2.getPieceSprite(), blackKingY + 1, blackKingX + 1);
                }
            } else if (currentType.equals("black")) {
                if (b_KingUnderAttack)
                    paintKingUnderAttack(blackKingX, blackKingY, board, gridPane);

                if (!b_KingUnderAttack && board[blackKingX][blackKingY].staleMate(board)) {
                    System.out.println("That's a TIE!!");
                    staleMateAlert();
                    gridPane.setDisable(true);
                }

                if (!w_KingUnderAttack) {
                    highLightBackGround(whiteKingX, whiteKingY, ((whiteKingX + whiteKingY) & 1) == 0 ? WHITE : GREY, gridPane);
                    Piece piece2 = board[whiteKingX][whiteKingY];
                    gridPane.getChildren().remove(piece2.getPieceSprite());
                    gridPane.add(piece2.getPieceSprite(), whiteKingY + 1, whiteKingX + 1);
                }
            }
        });
    }

}
