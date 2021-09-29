package sample;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public final class Constants {
    public static final int BOARD_HEIGHT = 8;
    public static final int BOARD_WIDTH = 8;
    public static final double OPTIMAL_SIZE = 64;
    public static final int MOVES_COUNT = 8;
    public static final int PADDING_VALUE = 10;
    public static final int OPTION_ICONS_SIZE = 30;

    // For the Board Background Color which is grey and white
    public static final Background GREY = new Background(new BackgroundFill(Color.GRAY, null, null));
    public static final Background WHITE = new Background(new BackgroundFill(Color.WHITE, null, null));
    public final Background red = new Background(new BackgroundFill(Color.RED, null, null));
}
