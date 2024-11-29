package net.dodo.crew.model;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import net.dodo.crew.model.enums.MissionCondition;
import net.dodo.crew.model.enums.MissionRestriction;
import net.dodo.crew.model.enums.Suit;

public class Mission {
  private int missionNumber = 1;
  private String description;
  private List<Task> tasks;
  private boolean completed;
  private int difficulty;
  private MissionCondition condition;
  private MissionRestriction restriction;
  private boolean timedMission;
  private Duration timeLimit;
  private Instant startTime;
  private int sonarTokens;
  private boolean firstTrickPlayed;
  private Card communicationCard;
  private Card lastWinningCard;
  private Player lastWinningPlayer;
  private List<Player> players;
  private List<Card> currentTrick;

  public Mission(int missionNumber, String description, int difficulty) {
    this.missionNumber = missionNumber;
    this.description = description;
    this.difficulty = difficulty;
    this.tasks = new ArrayList<>();
    this.completed = false;
    this.condition = MissionCondition.NONE;
    this.restriction = MissionRestriction.NONE;
    this.firstTrickPlayed = false;
    this.sonarTokens = -1; // -1 means unlimited tokens
    this.players = new ArrayList<>();
    this.currentTrick = new ArrayList<>();
    this.timedMission = false;
  }

  public void setTimeLimit(Duration timeLimit) {
    this.timeLimit = timeLimit;
    this.timedMission = true;
  }

  public void startTimer() {
    if (timedMission) {
      this.startTime = Instant.now();
    }
  }

  public Duration getRemainingTime() {
    if (!timedMission || startTime == null) {
      return Duration.ZERO;
    }
    Duration elapsed = Duration.between(startTime, Instant.now());
    return timeLimit.minus(elapsed);
  }

  public boolean isTimeExpired() {
    return timedMission && getRemainingTime().isNegative();
  }

  public void setCondition(MissionCondition condition) {
    this.condition = condition;
    if (condition == MissionCondition.RAPTURE_OF_DEEP) {
      this.sonarTokens = Math.max(1, tasks.size() - 2);
    }
  }

  public void setRestriction(MissionRestriction restriction) {
    this.restriction = restriction;
  }

  public boolean canPlayCard(Player player, Card card, boolean isFirstCard) {
    switch (restriction) {
      case NO_PINK_ROCKET_LEAD:
        if (isFirstCard && (card.getSuit().equals(Suit.PINK) || card.getSuit().equals(Suit.ROCKET))) {
          return false;
        }
        break;
      case NO_NINE_PAIR:
        if (card.getValue() == 9) {
          long playerNineCount = player.getHand().stream()
              .filter(c -> c.getValue() == 9)
              .count();
          long otherNineCount = players.stream()
              .filter(p -> p != player)
              .mapToLong(p -> p.getHand().stream()
                  .filter(c -> c.getValue() == 9)
                  .count())
              .max()
              .orElse(0);
          if (playerNineCount > otherNineCount + 2) {
            return false;
          }
        }
        break;
      case NO_WIN_WITH_PINK:
        if (card.getSuit().equals(Suit.PINK)  && wouldWinTrick(card)) {
          return false;
        }
        break;
      case NO_WIN_WITH_ROCKET:
        if (card.getSuit().equals(Suit.ROCKET)   && wouldWinTrick(card)) {
          return false;
        }
        break;
      case NO_ADJACENT_WINS:
        if (player == lastWinningPlayer && wouldWinTrick(card)) {
          return false;
        }
        break;
    }
    return true;
  }

  private boolean wouldWinTrick(Card newCard) {
    if (currentTrick.isEmpty()) {
      return true;
    }
    Card leadCard = currentTrick.get(0);
    for (Card card : currentTrick) {
      if (!newCard.wouldWin(card, leadCard.getSuit())) {
        return false;
      }
    }
    return true;
  }

  public void recordTrickWinner(Player player, Card winningCard) {
    this.lastWinningPlayer = player;
    this.lastWinningCard = winningCard;
  }

  public void addTask(Task task) {
    tasks.add(task);
  }

  public boolean checkCompletion() {
    if (isTimeExpired()) {
      return false;
    }
    completed = tasks.stream().allMatch(Task::isCompleted);
    return completed;
  }

  public boolean canCommunicate(Player player) {
    if (condition == MissionCondition.NO_COMMUNICATION) {
      return false;
    }
    if (condition == MissionCondition.DISTRESS_SIGNAL && !firstTrickPlayed) {
      return true;
    }
    if (condition == MissionCondition.CURRENTS) {
      return communicationCard == null;
    }
    if (condition == MissionCondition.RAPTURE_OF_DEEP) {
      return sonarTokens > 0;
    }
    return true;
  }

  public void useSonarToken() {
    if (sonarTokens > 0) {
      sonarTokens--;
    }
  }

  public void setCommunicationCard(Card card) {
    this.communicationCard = card;
  }

  public void firstTrickPlayed() {
    this.firstTrickPlayed = true;
  }

  public List<Task> getTasks() {
    return new ArrayList<>(tasks);
  }

  public int getMissionNumber() {
    return missionNumber;
  }

  public String getDescription() {
    StringBuilder desc = new StringBuilder(description);
    if (timedMission) {
      desc.append("\nTime Limit: ")
          .append(timeLimit.toMinutes())
          .append(":")
          .append(String.format("%02d", timeLimit.toSecondsPart()));
      if (condition != MissionCondition.NONE) {
        desc.append("\nIf playing without time limit: ")
            .append(condition.getDescription());
      }
    }
    if (restriction != MissionRestriction.NONE) {
      desc.append("\nSpecial Rule: ")
          .append(restriction.getDescription());
    }
    return desc.toString();
  }

  public boolean isCompleted() {
    return completed;
  }

  public int getDifficulty() {
    return difficulty;
  }

  public MissionCondition getCondition() {
    return condition;
  }

  public MissionRestriction getRestriction() {
    return restriction;
  }

  public boolean isTimedMission() {
    return timedMission;
  }

  public Duration getTimeLimit() {
    return timeLimit;
  }

  public int getSonarTokens() {
    return sonarTokens;
  }

  public boolean isFirstTrickPlayed() {
    return firstTrickPlayed;
  }

  public Card getCommunicationCard() {
    return communicationCard;
  }

  public void setPlayers(List<Player> players) {
    this.players = new ArrayList<>(players);
  }

  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  public List<Card> getCurrentTrick() {
    return new ArrayList<>(currentTrick);
  }

  public void setCurrentTrick(List<Card> currentTrick) {
    this.currentTrick = new ArrayList<>(currentTrick);
  }

  public void setTimedMission(boolean b) {
    this.timedMission = b;


  }

  public Mission setMissionNumber(int missionNumber) {
    this.missionNumber = missionNumber;
    return this;
  }

  public Mission setDescription(String description) {
    this.description = description;
    return this;
  }

  public Mission setTasks(List<Task> tasks) {
    this.tasks = tasks;
    return this;
  }

  public Mission setCompleted(boolean completed) {
    this.completed = completed;
    return this;
  }

  public Mission setDifficulty(int difficulty) {
    this.difficulty = difficulty;
    return this;
  }

  public Instant getStartTime() {
    return startTime;
  }

  public Mission setStartTime(Instant startTime) {
    this.startTime = startTime;
    return this;
  }

  public Mission setSonarTokens(int sonarTokens) {
    this.sonarTokens = sonarTokens;
    return this;
  }

  public Mission setFirstTrickPlayed(boolean firstTrickPlayed) {
    this.firstTrickPlayed = firstTrickPlayed;
    return this;
  }

  public Card getLastWinningCard() {
    return lastWinningCard;
  }

  public Mission setLastWinningCard(Card lastWinningCard) {
    this.lastWinningCard = lastWinningCard;
    return this;
  }

  public Player getLastWinningPlayer() {
    return lastWinningPlayer;
  }

  public Mission setLastWinningPlayer(Player lastWinningPlayer) {
    this.lastWinningPlayer = lastWinningPlayer;
    return this;
  }
}
