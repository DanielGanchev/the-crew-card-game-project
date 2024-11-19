package net.dodo.crew.model;

public class Task {
  private Card targetCard;
  private boolean completed = false;
  private int difficulty3Players;
  private int difficulty4Players;
  private int difficulty5Players;

  public Task(Card targetCard, int difficulty3Players, int difficulty4Players, int difficulty5Players) {
    this.targetCard = targetCard;
    this.difficulty3Players = difficulty3Players;
    this.difficulty4Players = difficulty4Players;
    this.difficulty5Players = difficulty5Players;
  }

  public Card getTargetCard() {
    return targetCard;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public int getDifficulty(int playerCount) {
    switch (playerCount) {
      case 3:
        return difficulty3Players;
      case 4:
        return difficulty4Players;
      case 5:
        return difficulty5Players;
      default:
        throw new IllegalArgumentException("Invalid player count: " + playerCount);
    }
  }
}
