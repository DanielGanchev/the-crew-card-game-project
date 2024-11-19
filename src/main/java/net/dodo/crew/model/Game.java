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

  public Game() {
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

  public void startGame(int difficulty) {
    if (players.isEmpty()) {
      throw new IllegalStateException("Cannot start game with no players.");
    }
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
}
