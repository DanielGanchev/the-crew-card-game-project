package net.dodo.crew.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.dodo.crew.model.enums.Suit;

public class Deck {
  private final List<Card> cards = new ArrayList<>();

  public Deck() {
    initializeDeck();
  }

  private void initializeDeck() {

    for (Suit suit : Suit.values()) {
      if (suit == Suit.ROCKETS) {
        for (int i = 1; i <= 4; i++) {
          cards.add(new Card(suit, 0));
        }
      } else {
        for (int i = 1; i <= 9; i++) {
          cards.add(new Card(suit, i));
        }
      }
    }
  }

  public void shuffle() {
    Collections.shuffle(cards);
  }

  public Card deal() {
    if (cards.isEmpty()) return null;
    return cards.removeFirst();
  }
}
