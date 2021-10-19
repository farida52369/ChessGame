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

        Game game = new Game();
        Piece[][] board = game.getBoard();

        // For Image as an icon
        Image icon = new Image((Objects.requireNonNull(getClass().getResourceAsStream("../Images/icon.png"))));
        primaryStage.getIcons().add(icon);

        GridPane gridPane = new BoardVisualizer(board);


        MoveBoardUI moveBoardUI = new MoveBoardUI();
        moveBoardUI.selectPiece(gridPane, board);

        primaryStage.setScene(new Scene(gridPane));
        primaryStage.show();
    }

    public static void main(String[] args) {
        // 😀
        launch(args);
    }
}

