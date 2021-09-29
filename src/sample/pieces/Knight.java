package sample.pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.MoveList;

import java.util.Objects;

public class Knight extends Piece {

    public Knight(int rowPos, int colPos, Type type) {
        super(rowPos, colPos, type);
    }

    @Override
    public boolean useBaseMoves() {
        return true;
    }

    @Override
    public ImageView getPieceSprite() {
        String locationImage = String.format("../../Images/%s_knight.png", this.getType());
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(locationImage)));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(this.getOptimalSize());
        imageView.setFitHeight(this.getOptimalSize());
        return imageView;
    }

    @Override
    public MoveList[] getMoveList() {
        return new MoveList[] {
                MoveList.KNIGHT_RIGHT_UP,
                MoveList.KNIGHT_UP_RIGHT,
                MoveList.KNIGHT_LEFT_UP,
                MoveList.KNIGHT_UP_LEFT,

                MoveList.KNIGHT_RIGHT_DOWN,
                MoveList.KNIGHT_DOWN_RIGHT,
                MoveList.KNIGHT_LEFT_DOWN,
                MoveList.KNIGHT_DOWN_LEFT
        };
    }
}
