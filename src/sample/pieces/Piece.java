package sample.pieces;

import javafx.scene.image.ImageView;

public abstract class Piece {

    private ImageView pieceSprite;
    private int rowPos, colPos;

    // To Check if it's White Or Black Piece
    private Type type;

    // For Width and Height
    private final double optimalWidth = 64;

    public Piece(int rowPos, int colPos, Type type) {
        this.rowPos = rowPos;
        this.colPos = colPos;

        // Checking white Or black
        // To get the exact piece
        this.type = type;
    }

    public String getType() {
        if (this.type == Type.BLACK) return "black";
        else return "white";
    }

    public double getOptimalSize() {
        return this.optimalWidth;
    }


    // To get the ImageView of the Piece
    public abstract ImageView getPieceSprite();
}
