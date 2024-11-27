package net.dodo.crew.model;

import java.util.ArrayList;
import java.util.List;
import net.dodo.crew.model.enums.Suit;

public class Game {
  private static final int MAX_NUM_OF_PLAYERS = 5;
  private static final int MIN_NUM_OF_PLAYERS = 3;
  private Deck deck;
  private List<Player> players;
  private List<Card> currentTrick = new ArrayList<>();
  private Player trickLeader;
  private Suit trumpSuit = Suit.ROCKETS;
  private Level currentLevel;
  private List<Level> allLevels;

  public Game(List<Level> allLevels) {
    this.allLevels = allLevels;
    this.players = new ArrayList<>();
    deck = new Deck();
    deck.shuffle();
    dealCards();
    assignTasks();
  }

  public void addPlayer(Player player) {
    if (players.size() < MAX_NUM_OF_PLAYERS) {
      players.add(player);
    }
  }

  public void startGame(int levelNumber) {

    if (players.isEmpty()) {
      throw new IllegalStateException("Cannot start game with no players.");
    }
    levelNumber = (levelNumber != 0) ? levelNumber : 1;
    if (currentLevel == null) {
      throw new IllegalStateException("Level not found.");
    }
    if (currentLevel.getDrawnTasks().isEmpty()) {
      throw new IllegalStateException("No tasks found for this level.");
    }
    currentLevel = allLevels.get(levelNumber);
    currentLevel.applyConditions(this);
    deck.shuffle();
    dealCards();
    assignTasks();
  }

  private void dealCards() {
    for (int i = 0; i < 9; i++) { // Example deal 9 cards each
      for (Player player : players) {
        player.addCard(deck.deal());
      }
    }
    for (Player player : players) {
      if (player.getHand().contains(new Card(Suit.ROCKETS, 4))) {
        trickLeader = player;
        break;
      }
    }
  }

  private void assignTasks() {}

  public void playCard(Player player, Card card) {
    currentTrick.add(card);
    player.getHand().remove(card);

    if (currentTrick.size() == players.size()) {
      determineTrickWinner();
    }
  }

  private void determineTrickWinner() {
    Card leadCard = currentTrick.get(0);
    Suit leadSuit = leadCard.getSuit();
    Card winningCard = leadCard;
    Player winner = trickLeader;

    for (int i = 1; i < currentTrick.size(); i++) {
      Card card = currentTrick.get(i);
      Player currentPlayer = getNextPlayer(winner);

      if (card.getSuit() == Suit.ROCKETS && card.getValue() == 4) {
        winningCard = card;
        winner = currentPlayer;
      } else if (winningCard.getSuit() != Suit.ROCKETS || winningCard.getValue() != 4) {

        if (card.getSuit() == Suit.ROCKETS) {
          if (winningCard.getSuit() != Suit.ROCKETS || card.getValue() > winningCard.getValue()) {
            winningCard = card;
            winner = currentPlayer;
          }
        } else if (card.getSuit() == leadSuit && winningCard.getSuit() != Suit.ROCKETS) {
          if (card.getValue() > winningCard.getValue()) {
            winningCard = card;
            winner = currentPlayer;
          }
        }
      }
    }

    trickLeader = winner;
    currentTrick.clear();

    for (Player p : players) {
      checkTasks(p);
    }
  }

  private void checkTasks(Player player) {}

  private Player getNextPlayer(Player currentPlayer) {
    int index = players.indexOf(currentPlayer);
    return players.get((index + 1) % players.size());
  }

  public Deck getDeck() {
    return deck;
  }

  public Game setDeck(Deck deck) {
    this.deck = deck;
    return this;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public Game setPlayers(List<Player> players) {
    this.players = players;
    return this;
  }

  public List<Card> getCurrentTrick() {
    return currentTrick;
  }

  public Game setCurrentTrick(List<Card> currentTrick) {
    this.currentTrick = currentTrick;
    return this;
  }

  public Player getTrickLeader() {
    return trickLeader;
  }

  public Game setTrickLeader(Player trickLeader) {
    this.trickLeader = trickLeader;
    return this;
  }

  public Suit getTrumpSuit() {
    return trumpSuit;
  }

  public Game setTrumpSuit(Suit trumpSuit) {
    this.trumpSuit = trumpSuit;
    return this;
  }

  public static int getMaxNumOfPlayers() {
    return MAX_NUM_OF_PLAYERS;
  }

  public static int getMinNumOfPlayers() {
    return MIN_NUM_OF_PLAYERS;
  }

  public boolean playerExists(String playerName) {
    List<Player> players = getPlayers();
    for (Player player : players) {
      if (player.getName().equalsIgnoreCase(playerName)) {
        return true;
      }
    }
    return false;
  }

  public void applyConditions(int timeLimit) {
    currentLevel.applyConditions(this);
  }

  public void applyCurrentsRule() {
    System.out.println("Applying Currents rule...");
    // ... your game logic for Currents ...
  }

  public void applyRaptureRule() {
    System.out.println("Applying Rapture of the Deep rule...");
    // ... your game logic for Rapture ...
  }

  public void assignAllTasksToCaptain() {
    System.out.println("Assigning all tasks to the Captain...");
    // ... Assign tasks logic ...
  }

  public void setTimeLimit(int timeLimit) {
    System.out.println(
        "Setting time limit to "
            + timeLimit
            + " seconds for level "
            + currentLevel.getLevelNumber());

    if (timeLimit == 0) {
      System.out.println("No time limit for this level");
    }
    // ... your time limit logic ...

  }
}
