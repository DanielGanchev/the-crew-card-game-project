package net.dodo.crew.model;

import net.dodo.crew.model.Card;
import net.dodo.crew.model.Player;

public class Task {
  private Card targetCard;
  private Player assignedPlayer;
  private boolean completed;
  private boolean failed;
  private int orderNumber; // For ordered tasks, 0 means no specific order
  private boolean mustWinWithCard; // If true, player must win trick with this card
  private boolean mustLoseWithCard; // If true, player must lose trick with this card

  public Task(Card targetCard) {
    this.targetCard = targetCard;
    this.completed = false;
    this.failed = false;
    this.orderNumber = 0;
    this.mustWinWithCard = false;
    this.mustLoseWithCard = false;
  }

  public void assignTo(Player player) {
    this.assignedPlayer = player;
  }

  public void setOrderNumber(int order) {
    this.orderNumber = order;
  }

  public void setMustWinWithCard(boolean mustWin) {
    this.mustWinWithCard = mustWin;
    if (mustWin) {
      this.mustLoseWithCard = false;
    }
  }

  public void setMustLoseWithCard(boolean mustLose) {
    this.mustLoseWithCard = mustLose;
    if (mustLose) {
      this.mustWinWithCard = false;
    }
  }

  public boolean checkCompletion(Card playedCard, Player player, boolean wonTrick) {
    if (!completed && !failed && player == assignedPlayer && playedCard.equals(targetCard)) {
      if (mustWinWithCard && !wonTrick) {
        failed = true;
        return false;
      }
      if (mustLoseWithCard && wonTrick) {
        failed = true;
        return false;
      }
      completed = true;
      return true;
    }
    return false;
  }

  public boolean isCompleted() {
    return completed;
  }

  public boolean isFailed() {
    return failed;
  }

  public Card getTargetCard() {
    return targetCard;
  }

  public Player getAssignedPlayer() {
    return assignedPlayer;
  }

  public int getOrderNumber() {
    return orderNumber;
  }

  public boolean isMustWinWithCard() {
    return mustWinWithCard;
  }

  public boolean isMustLoseWithCard() {
    return mustLoseWithCard;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Task: ").append(targetCard.toString());
    if (orderNumber > 0) {
      sb.append(" (Order: ").append(orderNumber).append(")");
    }
    if (mustWinWithCard) {
      sb.append(" [Must win]");
    }
    if (mustLoseWithCard) {
      sb.append(" [Must lose]");
    }
    if (assignedPlayer != null) {
      sb.append(" - Assigned to: ").append(assignedPlayer.getName());
    }
    if (completed) {
      sb.append(" [✓]");
    } else if (failed) {
      sb.append(" [✗]");
    }
    return sb.toString();
  }
}
