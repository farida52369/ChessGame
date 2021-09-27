package sample.pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Knight extends Piece {

    public Knight(int rowPos, int colPos, Type type) {
        super(rowPos, colPos, type);
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
}
