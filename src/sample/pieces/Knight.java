package sample.pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.MoveList;

import java.util.Objects;

public class Knight extends Piece {

    private ImageView imageView;

    public Knight(int rowPos, int colPos, Type type) {
        super(rowPos, colPos, type);
        this.setPieceSprite(this.getImage());
    }

    @Override
    public Image getImage() {
        String locationImage = String.format("../../Images/%s_knight.png", this.getType());
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
        return new MoveList[]{
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
