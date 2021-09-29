package sample.pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.MoveList;

import java.util.Objects;

public class Queen extends Piece {

    public Queen(int rowPos, int colPos, Type type) {
        super(rowPos, colPos, type);
    }

    @Override
    public boolean useBaseMoves() {
        return false;
    }

    @Override
    public ImageView getPieceSprite() {
        String locationImage = String.format("../../Images/%s_queen.png", this.getType());
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(locationImage)));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(this.getOptimalSize());
        imageView.setFitHeight(this.getOptimalSize());
        return imageView;
    }

    @Override
    public MoveList[] getMoveList() {
        return new MoveList[]{
                MoveList.UP,
                MoveList.DOWN,
                MoveList.RIGHT,
                MoveList.LEFT,

                MoveList.DIAGONAL_RIGHT_UP,
                MoveList.DIAGONAL_RIGHT_DOWN,
                MoveList.DIAGONAL_LEFT_UP,
                MoveList.DIAGONAL_LEFT_DOWN
        };
    }

}
