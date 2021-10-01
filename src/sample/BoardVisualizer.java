package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import sample.pieces.Piece;

import java.util.Objects;

import static sample.Constants.*;

public class BoardVisualizer extends GridPane {

    // Save and Load
    private final ImageView saveIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../Images/save.png"))));
    private final ImageView resetIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../Images/reset.png"))));

    // For the Numbers and Letters in The Chess Board
    private Label newRowLabel(int i) {
        Label l = new Label(BOARD_HEIGHT - i + "");
        l.setMinSize(20, OPTIMAL_SIZE);
        l.setAlignment(Pos.CENTER);
        return l;
    }

    private Label newColLabel(int i) {
        Label l = new Label((char) (i + 65) + "");
        l.setMinSize(OPTIMAL_SIZE, 20);
        l.setAlignment(Pos.CENTER);
        return l;
    }

    public GridPane visualize(Piece[][] board) {

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(PADDING_VALUE, PADDING_VALUE, PADDING_VALUE, PADDING_VALUE));

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            // add method in GridPane takes the coordinates and (rowSpan, colSpan) are optional
            gridPane.add(newRowLabel(i), 0, i + 1, 1, 1);
            gridPane.add(newRowLabel(i), 9, i + 1, 1, 1);
            gridPane.add(newColLabel(i), i + 1, 0, 1, 1);
            gridPane.add(newColLabel(i), i + 1, 9, 1, 1);
        }


        // For the Background Colors
        for (int i = 1; i <= BOARD_WIDTH; i++) {
            for (int j = 1; j <= BOARD_HEIGHT; j++) {
                StackPane field = new StackPane();
                field.setBackground(((i + j) & 1) == 0 ? WHITE : GREY);
                gridPane.add(field, i, j);
            }
        }

        // For the Pieces
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (board[i][j] != null)
                    gridPane.add(board[i][j].getPieceSprite(), j + 1, i + 1);
            }
        }


        // Save Icon
        saveIcon.setFitHeight(OPTION_ICONS_SIZE);
        saveIcon.setFitWidth(OPTION_ICONS_SIZE);
        gridPane.add(saveIcon, 7, 11);

        // Reset Icon
        resetIcon.setFitHeight(OPTION_ICONS_SIZE);
        resetIcon.setFitWidth(OPTION_ICONS_SIZE);
        gridPane.add(resetIcon, 8, 11);

        return gridPane;

    }
}
