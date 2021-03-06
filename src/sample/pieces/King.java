package sample.pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.MoveList;

import java.util.Objects;

public class King extends Piece {

    private ImageView imageView;

    public King(int rowPos, int colPos, Type type) {
        super(rowPos, colPos, type);
        this.setPieceSprite(this.getImage());
    }

    @Override
    public boolean useBaseMoves() {
        return true;
    }

    @Override
    public Image getImage() {
        String locationImage = String.format("../../Images/%s_king.png", this.getType());
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(locationImage)));
    }

    @Override
    public void setPieceSprite(Image image) {
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(this.getOptimalSize());
        this.imageView.setFitHeight(this.getOptimalSize());
    }

    @Override
    public ImageView getPieceSprite() {

        return imageView;
    }

    @Override
    public MoveList[] getMoveList() {

        return new MoveList[]{
                MoveList.UP,
                MoveList.DOWN,
                MoveList.RIGHT,
                MoveList.LEFT,

                MoveList.DIAGONAL_LEFT_DOWN,
                MoveList.DIAGONAL_RIGHT_DOWN,
                MoveList.DIAGONAL_LEFT_UP,
                MoveList.DIAGONAL_RIGHT_UP
        };
    }
}
