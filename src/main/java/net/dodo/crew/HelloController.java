package net.dodo.crew;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.dodo.crew.model.Game;
import net.dodo.crew.model.Player;

public class HelloController {

  private Game game = new Game();

  @FXML private Label welcomeText;

  @FXML
  protected void onHelloButtonClick() {
    welcomeText.setText("Welcome to JavaFX Application!");
  }

  @FXML private TextField playerNameField;

  @FXML
  private void addPlayer() {
    String playerName = playerNameField.getText();
    if (!playerName.isEmpty()) {
      game.addPlayer(new Player(playerName));
      playerNameField.clear();
    }
  }
}
