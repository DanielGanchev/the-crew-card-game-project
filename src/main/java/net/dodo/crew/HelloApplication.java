package net.dodo.crew;

import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.dodo.crew.model.Game;
import net.dodo.crew.model.Player;

public class HelloApplication extends Application {

  private Game game = new Game();

  @Override
  public void start(Stage stage) throws IOException {
    VBox root = new VBox(10);
    root.setPadding(new Insets(20));
    root.setAlignment(Pos.CENTER); // Center align content

    Label playerNameLabel = new Label("Player Name:");
    TextField playerNameField = new TextField();
    Button addPlayerButton = new Button("Add Player");

    HBox addPlayerBox = new HBox(10); // Container for name field and button
    addPlayerBox.setAlignment(Pos.CENTER);
    addPlayerBox.getChildren().addAll(playerNameLabel, playerNameField, addPlayerButton);

    addPlayerButton.setOnAction(
        event -> {
          String playerName = playerNameField.getText().trim();
          if (game.getPlayers().size() >= Game.getMaxNumOfPlayers()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Player Limit");
            alert.setHeaderText(null);
            alert.setContentText("Maximum " + Game.getMaxNumOfPlayers() + " players allowed!");
            alert.showAndWait();

          } else if (game.playerExists(playerName)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplicate Player Name");
            alert.setHeaderText(null);
            alert.setContentText(
                "Player '" + playerName + "' already exists. Please enter a different name.");
            alert.showAndWait();

          } else if (!playerName.isEmpty()) {
            game.addPlayer(new Player(playerName));
            playerNameField.clear();
            System.out.println(playerName + " added.");

          } else {
            System.out.println("Player name cannot be empty.");
          }
        });

    Button startGameButton = new Button("Start Game");
    startGameButton.setOnAction(
        event -> {
          if (game.getPlayers().size() < Game.getMinNumOfPlayers()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not Enough Players");
            alert.setHeaderText(null);
            alert.setContentText(
                "Minimum "
                    + Game.getMinNumOfPlayers()
                    + " players are required to start the game.");
            alert.showAndWait();
          } else if (game.getPlayers().size() > Game.getMaxNumOfPlayers()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Too Many Players");
            alert.setHeaderText(null);
            alert.setContentText("Maximum " + Game.getMaxNumOfPlayers() + " players are allowed.");
            alert.showAndWait();
          } else {
            try {
              game.startGame(5);
              System.out.println("Game started!");

            } catch (IllegalStateException e) {
              System.err.println(e.getMessage());
            }
          }
        });

    root.getChildren().addAll(addPlayerBox, startGameButton);
    Scene scene = new Scene(root, 300, 200);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
