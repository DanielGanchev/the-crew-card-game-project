package net.dodo.crew.model;

import java.util.ArrayList;
import java.util.List;
import net.dodo.crew.model.enums.Suit;

public class Game {
  private List<Player> players;
  private Deck deck;
  private int currentPlayerIndex;
  private List<Card> currentTrick;
  private Suit currentTrickSuit;
  private Mission currentMission;

  public Game() {
    this.players = new ArrayList<>();
    this.deck = new Deck();
    this.currentTrick = new ArrayList<>();
  }

  public void setCurrentMission(Mission mission) {
    this.currentMission = mission;
  }

  public void addPlayer(Player player) {
    if (players.size() >= 5) {
      throw new IllegalStateException("Maximum 5 players allowed");
    }
    players.add(player);
  }

  public void startGame() {
    if (players.size() < 3) {
      throw new IllegalStateException("Minimum 3 players required");
    }
    if (players.size() > 5) {
      throw new IllegalStateException("Maximum 5 players allowed");
    }
    if (currentMission == null) {
      setCurrentMission(MissionFactory.createMission(1, players.size()));
    }

    deck.shuffle();

    // Calculate cards per player - all cards must be distributed
    int totalCards = 40; // 4 suits × 9 cards + 4 rockets
    int cardsPerPlayer = totalCards / players.size();
    int remainingCards = totalCards % players.size();


    // Distribute cards
    for (Player player : players) {
      int playerCards = cardsPerPlayer;
      if (remainingCards > 0) {
        playerCards++;
        remainingCards--;
      }

      for (int i = 0; i < playerCards; i++) {
        Card card = deck.drawCard();
        if (card != null) {
          player.addCardToHand(card);
          // Assign commander to player with highest rocket
          if (card.getSuit() == Suit.ROCKET && card.getValue() == 4) {
            player.setCommander(true);
            currentPlayerIndex = players.indexOf(player);
          }
        }
      }
    }
  }

  public Player getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  public void playCard(Player player, Card card) {
    if (currentMission == null) {
      throw new IllegalStateException("Current mission is not set");
    }

    if (player != getCurrentPlayer()) {
      throw new IllegalStateException("Not this player's turn");
    }

    if (!currentMission.canPlayCard(player, card, currentTrick.isEmpty())) {
      throw new IllegalStateException("Cannot play this card due to mission restrictions");
    }

    if (currentTrick.isEmpty()) {
      currentTrickSuit = card.getSuit();
      if (!currentMission.isFirstTrickPlayed()) {
        currentMission.firstTrickPlayed();
      }
    }

    currentTrick.add(card);

    if (currentTrick.size() == players.size()) {
      resolveCurrentTrick();
    } else {
      currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
  }

  private void resolveCurrentTrick() {
    int winningCardIndex = 0;
    Card winningCard = currentTrick.get(0);

    for (int i = 1; i < currentTrick.size(); i++) {
      Card card = currentTrick.get(i);

      if (card.getSuit() == Suit.ROCKET) {
        if (winningCard.getSuit() != Suit.ROCKET ||
            card.getValue() > winningCard.getValue()) {
          winningCard = card;
          winningCardIndex = i;
        }
      }
      else if (card.getSuit().equals(currentTrickSuit)) {
        if (winningCard.getSuit() != Suit.ROCKET &&
            card.getValue() > winningCard.getValue()) {
          winningCard = card;
          winningCardIndex = i;
        }
      }
    }

    // Record trick winner for mission conditions
    Player winner = players.get((currentPlayerIndex + winningCardIndex) % players.size());
    currentMission.recordTrickWinner(winner, winningCard);

    // Check task completion
    for (Task task : currentMission.getTasks()) {
      if (!task.isCompleted()) {
        task.checkCompletion(winningCard, winner, true);
      }
    }

    // Winner of trick starts next trick
    currentPlayerIndex = (currentPlayerIndex + winningCardIndex) % players.size();
    currentTrick.clear();
    currentTrickSuit = null;
  }

  public List<Card> getCurrentTrick() {
    return new ArrayList<>(currentTrick);
  }

  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  public Mission getCurrentMission() {
    return currentMission;
  }
}
