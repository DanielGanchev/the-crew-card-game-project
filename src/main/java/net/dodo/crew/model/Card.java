package net.dodo.crew.model;

import net.dodo.crew.model.enums.Suit;

public class Card {

  private Suit suit;
  private int value;

  public Card(Suit suit, int value) {
    this.suit = suit;
    this.value = value;
  }

  public Suit getSuit() {
    return suit;
  }

  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value + " of " + suit;
  }
}
