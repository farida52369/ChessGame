package sample.pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.MoveList;

import java.util.Objects;

public class Queen extends Piece {

    private ImageView imageView;

    public Queen(int rowPos, int colPos, Type type) {
        super(rowPos, colPos, type);
        this.setPieceSprite(this.getImage());
    }

    @Override
    public Image getImage() {
        String locationImage = String.format("../../Images/%s_queen.png", this.getType());
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(locationImage)));
    }

    @Override
    public boolean useBaseMoves() {
        return false;
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
