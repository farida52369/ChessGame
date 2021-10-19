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
    public static final double LABEL_SIZE = 20;
    public static final double HV_GAP = 0.5;

    // For the Board Background Color which is grey and white
    public static final Background GREY = new Background(new BackgroundFill(Color.GRAY, null, null));
    public static final Background WHITE = new Background(new BackgroundFill(Color.WHITE, null, null));
    public static final Background BLACK_RED = new Background(new BackgroundFill(Color.rgb(128, 0, 0), null, null));
    public static final Background WHITE_RED = new Background(new BackgroundFill(Color.rgb(165, 42, 42), null, null));
    public static final Background BLACK_BLUE = new Background(new BackgroundFill(Color.rgb(0, 191, 255), null, null));
    public static final Background WHITE_BLUE = new Background(new BackgroundFill(Color.rgb(135, 206, 250), null, null));
}
