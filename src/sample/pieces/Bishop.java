package sample.pieces;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.MoveList;

import java.util.Objects;


public class Bishop extends Piece {
    public Bishop(int rowPos, int colPos, Type type) {
        super(rowPos, colPos, type);
    }

    @Override
    public boolean useBaseMoves() {
        return false;
    }

    @Override
    public ImageView getPieceSprite() {
        String locationImage = String.format("../../Images/%s_bishop.png", this.getType());
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(locationImage)));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(this.getOptimalSize());
        imageView.setFitHeight(this.getOptimalSize());
        return imageView;
    }

    @Override
    public MoveList[] getMoveList() {
        return new MoveList[] {
          MoveList.DIAGONAL_LEFT_DOWN,
          MoveList.DIAGONAL_RIGHT_DOWN,
          MoveList.DIAGONAL_LEFT_UP,
          MoveList.DIAGONAL_RIGHT_UP
        };
    }

}
