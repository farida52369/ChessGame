package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import sample.pieces.Piece;

import static sample.Constants.*;

public class BoardVisualizer extends GridPane {

    /*
     * Future Development
    // Save and Load
    private final ImageView saveIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../Images/save.png"))));
    private final ImageView resetIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../Images/reset.png"))));
    */

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


    public BoardVisualizer(Piece[][] board) {

        // Properties for the GridPane
        setPadding(new Insets(PADDING_VALUE, PADDING_VALUE, PADDING_VALUE, PADDING_VALUE));
        setHgap(HV_GAP);
        setVgap(HV_GAP);

        // Numbers and Letters in the Board
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            // add method in GridPane takes the coordinates and (rowSpan, colSpan) are optional
            add(newRowLabel(i), 0, i + 1, 1, 1);
            add(newRowLabel(i), 9, i + 1, 1, 1);
            add(newColLabel(i), i + 1, 0, 1, 1);
            add(newColLabel(i), i + 1, 9, 1, 1);
        }

        // For the Background Colors
        for (int i = 1; i <= BOARD_WIDTH; i++) {
            for (int j = 1; j <= BOARD_HEIGHT; j++) {
                StackPane field = new StackPane();
                field.setBackground(((i + j) & 1) == 0 ? WHITE : GREY);
                add(field, i, j);
            }
        }

        // For the Pieces
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (board[i][j] != null)
                    add(board[i][j].getPieceSprite(), j + 1, i + 1);
            }
        }

        /*
         * Future Development
        // Save Icon
        saveIcon.setFitHeight(OPTION_ICONS_SIZE);
        saveIcon.setFitWidth(OPTION_ICONS_SIZE);
        add(saveIcon, 7, 11);

        // Reset Icon
        resetIcon.setFitHeight(OPTION_ICONS_SIZE);
        resetIcon.setFitWidth(OPTION_ICONS_SIZE);
        add(resetIcon, 8, 11);
        */
    }
}
