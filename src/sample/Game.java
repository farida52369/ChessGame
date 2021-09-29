package sample;

import sample.pieces.*;
import static sample.Constants.*;

public class Game {

    // For the board --
    private final Piece[][] board;

    public Game() {

        board = new Piece[BOARD_WIDTH][BOARD_HEIGHT];
        // Black
        this.board[0][0] = new Rook(0, 0, Type.BLACK);
        this.board[0][1] = new Knight(0, 1, Type.BLACK);
        this.board[0][2] = new Bishop(0, 2, Type.BLACK);
        this.board[0][3] = new Queen(0, 3, Type.BLACK);
        this.board[0][4] = new King(0, 4, Type.BLACK);
        this.board[0][5] = new Bishop(0, 5, Type.BLACK);
        this.board[0][6] = new Knight(0, 6, Type.BLACK);
        this.board[0][7] = new Rook(0, 7, Type.BLACK);

        // Pawn Black
        for (int i = 0; i < BOARD_WIDTH; i++) {
            this.board[1][i] = new Pawn(1, i, Type.BLACK);
        }

        // White
        this.board[7][0] = new Rook(7, 0, Type.WHITE);
        this.board[7][1] = new Knight(7, 1, Type.WHITE);
        this.board[7][2] = new Bishop(7, 2, Type.WHITE);
        this.board[7][3] = new Queen(7, 3, Type.WHITE);
        this.board[7][4] = new King(7, 4, Type.WHITE);
        this.board[7][5] = new Bishop(7, 5, Type.WHITE);
        this.board[7][6] = new Knight(7, 6, Type.WHITE);
        this.board[7][7] = new Rook(7, 7, Type.WHITE);

        // Pawn White
        for (int i = 0; i < BOARD_WIDTH; i++) {
            this.board[6][i] = new Pawn(6, i, Type.WHITE);
        }
    }

    public Piece[][] getBoard() {
        return this.board;
    }

}
