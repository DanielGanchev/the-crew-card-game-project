package net.dodo.crew;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


import java.util.*;
import net.dodo.crew.model.Card;
import net.dodo.crew.model.Game;
import net.dodo.crew.model.Player;

public class HelloApplication extends Application {
  private Game game;
  private BorderPane root;
  private HBox playerHand;
  private VBox centerArea;
  private Label statusLabel;
  private HBox currentTrick;
  private Stage primaryStage;
  private List<String> playerNames = new ArrayList<>();

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    showPlayerSetup();
  }

  private void showPlayerSetup() {
    VBox setupRoot = new VBox(20);
    setupRoot.setAlignment(Pos.CENTER);
    setupRoot.setPadding(new Insets(20));
    setupRoot.setStyle("-fx-background-color: #2C3E50;");

    Label titleLabel = new Label("The Crew - Player Setup");
    titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
    titleLabel.setStyle("-fx-text-fill: white;");

    ComboBox<Integer> playerCountBox = new ComboBox<>();
    playerCountBox.getItems().addAll(3, 4, 5);
    playerCountBox.setValue(3);
    playerCountBox.setStyle("""
            -fx-background-color: white;
            -fx-font-size: 16px;
        """);

    VBox playerInputs = new VBox(10);
    playerInputs.setAlignment(Pos.CENTER);

    Button startButton = new Button("Start Game");
    startButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    startButton.setStyle("""
            -fx-background-color: #27ae60;
            -fx-text-fill: white;
            -fx-padding: 10 20;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
        """);

    Label errorLabel = new Label("");
    errorLabel.setStyle("-fx-text-fill: #e74c3c;");


    playerCountBox.setOnAction(e -> {
      playerInputs.getChildren().clear();
      for (int i = 0; i < playerCountBox.getValue(); i++) {
        TextField nameField = new TextField();
        nameField.setPromptText("Player " + (i + 1) + " name");
        nameField.setStyle("""
                    -fx-background-color: white;
                    -fx-font-size: 16px;
                    -fx-padding: 8 15;
                """);
        playerInputs.getChildren().add(nameField);
      }
    });
    playerCountBox.fireEvent(new javafx.event.ActionEvent());

    startButton.setOnAction(e -> {
      playerNames.clear();
      Set<String> uniqueNames = new HashSet<>();
      boolean hasEmptyName = false;

      for (javafx.scene.Node node : playerInputs.getChildren()) {
        if (node instanceof TextField) {
          String name = ((TextField) node).getText().trim();
          if (name.isEmpty()) {
            hasEmptyName = true;
            break;
          }
          playerNames.add(name);
          uniqueNames.add(name);
        }
      }

      if (hasEmptyName) {
        errorLabel.setText("All players must have names!");
        return;
      }

      if (uniqueNames.size() != playerNames.size()) {
        errorLabel.setText("All player names must be unique!");
        return;
      }

      initializeGame();
      createUI();
      Scene gameScene = new Scene(root, 1024, 768);
      primaryStage.setScene(gameScene);
    });

    setupRoot.getChildren().addAll(
        titleLabel,
        new Label("Select number of players:") {{
          setStyle("-fx-text-fill: white;");
        }},
        playerCountBox,
        playerInputs,
        startButton,
        errorLabel
    );

    Scene setupScene = new Scene(setupRoot, 600, 400);
    primaryStage.setTitle("The Crew Card Game");
    primaryStage.setScene(setupScene);
    primaryStage.show();
  }

  private void initializeGame() {
    game = new Game();
    for (String playerName : playerNames) {
      game.addPlayer(new Player(playerName));
    }
    game.startGame();
  }

  private void createUI() {
    root = new BorderPane();
    root.setPadding(new Insets(20));
    root.setStyle("-fx-background-color: #2C3E50;");

    playerHand = new HBox(10);
    playerHand.setAlignment(Pos.CENTER);
    playerHand.setPadding(new Insets(10));

    centerArea = new VBox(20);
    centerArea.setAlignment(Pos.CENTER);

    statusLabel = new Label("Game Started!");
    statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    statusLabel.setStyle("-fx-text-fill: white;");

    currentTrick = new HBox(10);
    currentTrick.setAlignment(Pos.CENTER);
    currentTrick.setPadding(new Insets(20));

    centerArea.getChildren().addAll(statusLabel, currentTrick);
    root.setCenter(centerArea);
    root.setBottom(playerHand);

    updateUI();
  }

  private void updatePlayerHand() {
    playerHand.getChildren().clear();
    Player currentPlayer = game.getCurrentPlayer();
    List<Card> hand = currentPlayer.getHand();

    for (int i = 0; i < hand.size(); i++) {
      Card card = hand.get(i);
      Button cardButton = createCardButton(card, i);
      playerHand.getChildren().add(cardButton);
    }
  }

  private Button createCardButton(Card card, int index) {
    Button cardButton = new Button(card.toString());
    cardButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
    cardButton.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: #2C3E50;
            -fx-padding: 10 20;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);
        """);

    cardButton.setOnMouseEntered(e -> cardButton.setStyle("""
            -fx-background-color: #f8f9fa;
            -fx-text-fill: #2C3E50;
            -fx-padding: 10 20;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 3);
        """));

    cardButton.setOnMouseExited(e -> cardButton.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: #2C3E50;
            -fx-padding: 10 20;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);
        """));

    final int cardIndex = index;
    cardButton.setOnAction(e -> playCard(cardIndex));
    return cardButton;
  }

  private void playCard(int cardIndex) {
    Player currentPlayer = game.getCurrentPlayer();
    Card playedCard = currentPlayer.playCard(cardIndex);
    if (playedCard != null) {
      game.playCard(currentPlayer, playedCard);
      updateUI();
    }
  }

  private void updateUI() {
    updatePlayerHand();
    Player currentPlayer = game.getCurrentPlayer();
    statusLabel.setText(currentPlayer.getName() + "'s turn");

    currentTrick.getChildren().clear();
    for (Card card : game.getCurrentTrick()) {
      Label cardLabel = new Label(card.toString());
      cardLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
      cardLabel.setStyle("-fx-text-fill: white;");
      currentTrick.getChildren().add(cardLabel);
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
