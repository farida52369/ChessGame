package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sample.pieces.Piece;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chess Game");

        BoardVisualizer initializer = new BoardVisualizer();
        Game game = new Game();

        // Trying a move manually
        game.moveProcess(new Transition(6, 0, 5, 0));

        GridPane grid = initializer.visualize(game.getBoard());

        // For Image as an icon
        Image icon = new Image((Objects.requireNonNull(getClass().getResourceAsStream("../Images/icon.png"))));
        primaryStage.getIcons().add(icon);

        primaryStage.setScene(new Scene(grid));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

