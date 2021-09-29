package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chess Game");

        BoardVisualizer initializer = new BoardVisualizer();
        Game game = new Game();
        GridPane grid = initializer.visualize(game.getBoard());

        primaryStage.setScene(new Scene(grid));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

