package net.dodo.crew.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
  private String name;
  private List<Card> hand;
  private boolean isCommander;

  public Player(String name) {
    this.name = name;
    this.hand = new ArrayList<>();
    this.isCommander = false;
  }

  public void addCardToHand(Card card) {
    if (card != null) {
      hand.add(card);
    }
  }

  public Card playCard(int index) {
    if (index < 0 || index >= hand.size()) {
      return null;
    }
    return hand.remove(index);
  }

  public List<Card> getHand() {
    return new ArrayList<>(hand);
  }

  public String getName() {
    return name;
  }

  public boolean isCommander() {
    return isCommander;
  }

  public void setCommander(boolean commander) {
    isCommander = commander;
  }

  @Override
  public String toString() {
    return name;
  }
}
