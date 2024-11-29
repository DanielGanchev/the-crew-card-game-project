package net.dodo.crew.model;

import net.dodo.crew.model.enums.Suit;

public class Card {


  private final int value;
  private final Suit suit;

  public Card(int value, Suit suit) {
    this.value = value;
    this.suit = suit;
  }

  public Card(int value) {
    this.value = value;
    this.suit = Suit.ROCKET;
  }

  public int getValue() {
    return value;
  }

  public Suit getSuit() {
    return suit;
  }

  public boolean isRocket() {
    return suit == Suit.ROCKET;
  }

  public boolean wouldWin(Card otherCard, Suit leadSuit) {
    // Rockets always win
    if (this.suit == Suit.ROCKET) {
      if (otherCard.suit == Suit.ROCKET) {
        return this.value > otherCard.value;
      }
      return true;
    }
    if (otherCard.suit == Suit.ROCKET) {
      return false;
    }

    // Must follow suit if possible
    if (this.suit == leadSuit) {
      if (otherCard.suit == leadSuit) {
        return this.value > otherCard.value;
      }
      return true;
    }
    if (otherCard.suit == leadSuit) {
      return false;
    }

    // If neither card follows suit, higher value wins
    return this.value > otherCard.value;
  }

  @Override
  public String toString() {
    String suitSymbol = switch (suit) {
      case PINK -> "💗";
      case BLUE -> "💙";
      case YELLOW -> "💛";
      case GREEN -> "💚";
      case ROCKET -> "🚀";
    };
    return suitSymbol + " " + value;
  }
}
