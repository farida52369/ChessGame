package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import sample.pieces.Piece;

import java.util.Objects;

public class BoardVisualizer extends GridPane {

    // For the Board Background Color which is grey and white
    private final Background grey = new Background(new BackgroundFill(Color.GRAY, null, null));
    private final Background white = new Background(new BackgroundFill(Color.WHITE, null, null));
    private final Background red = new Background(new BackgroundFill(Color.RED, null, null));

    // Save and Load
    private ImageView save = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../Images/save.png"))));
    private ImageView reset = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../Images/reset.png"))));

    // For the Numbers and Letters in The Chess Board
    private Label newRowLabel(int i) {
        Label l = new Label(8 - i + "");
        l.setMinSize(20, 64);
        l.setAlignment(Pos.CENTER);
        return l;
    }

    private Label newColLabel(int i) {
        Label l = new Label((char) (i + 65) + "");
        l.setMinSize(64, 20);
        l.setAlignment(Pos.CENTER);
        return l;
    }

    public GridPane visualize(Piece[][] board) {

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        for (int i = 0; i < 8; i++) {
            // add method in GridPane takes the coordinates and (rowSpan, colSpan) are optional
            gridPane.add(newRowLabel(i), 0, i + 1, 1, 1);
            gridPane.add(newRowLabel(i), 9, i + 1, 1, 1);
            gridPane.add(newColLabel(i), i + 1, 0, 1, 1);
            gridPane.add(newColLabel(i), i + 1, 9, 1, 1);
        }


        // For the Background Colors
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                StackPane field = new StackPane();
                field.setBackground(((i + j) & 1) == 0 ? white : grey);
                gridPane.add(field, i, j);
            }
        }

        // For the Pieces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null)
                    gridPane.add(board[i][j].getPieceSprite(), j + 1, i + 1);
            }
        }


        // Save Icon
        save.setFitHeight(30);
        save.setFitWidth(30);
        gridPane.add(save, 7, 11);

        // Reset Icon
        reset.setFitHeight(30);
        reset.setFitWidth(30);
        gridPane.add(reset, 8, 11);

        return gridPane;

    }
}
