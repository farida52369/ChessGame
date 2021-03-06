package sample.pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.MoveList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pawn extends Piece {

    private ImageView imageView;

    public Pawn(int rowPos, int colPos, Type type) {
        super(rowPos, colPos, type);
        this.setPieceSprite(this.getImage());

    }

    @Override
    public Image getImage() {
        String locationImage = String.format("../../Images/%s_pawn.png", this.getType());
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(locationImage)));
    }

    @Override
    public boolean useBaseMoves() {
        return true;
    }

    @Override
    public void setPieceSprite(Image image) {
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(this.getOptimalSize());
        this.imageView.setFitHeight(this.getOptimalSize());
    }

    @Override
    public ImageView getPieceSprite() {
        return this.imageView;
    }


    @Override
    public MoveList[] getMoveList() {

        MoveList[] moves = {};

        // As PAWN Only moves forward, Not like other Pieces.
        // IF Black?
        if (this.getType().equals("black")) {
            List<MoveList> whiteArr = new ArrayList<>();

            // For The First Move available Two Squares
            if (getHasMoved()) whiteArr.add(MoveList.DOUBLE_UP);

            // For All The Time Available One Square UP
            whiteArr.add(MoveList.DOWN);

            // For Capturing Technique
            whiteArr.add(MoveList.DIAGONAL_RIGHT_DOWN);
            whiteArr.add(MoveList.DIAGONAL_LEFT_DOWN);

            moves = whiteArr.toArray(moves);
        } else {
            // IF White?
            List<MoveList> blackArr = new ArrayList<>();

            // For The First Move available Two Squares
            if (getHasMoved()) blackArr.add(MoveList.DOUBLE_DOWN);

            // For All The Time Available One Square UP
            blackArr.add(MoveList.UP);

            // For Capturing Technique
            blackArr.add(MoveList.DIAGONAL_RIGHT_UP);
            blackArr.add(MoveList.DIAGONAL_LEFT_UP);

            moves = blackArr.toArray(moves);
        }
        return moves;
    }
}
