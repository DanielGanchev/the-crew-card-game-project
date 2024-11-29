package net.dodo.crew.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.dodo.crew.model.enums.Suit;

public class Deck {
  private List<Card> cards;

  public Deck() {
    cards = new ArrayList<>();
    initializeDeck();
  }

  private void initializeDeck() {
    // Add colored cards (1-9 in each suit and 1-4 rockets)
    for (Suit suit : Suit.values()) {
      if (suit != Suit.ROCKET) {
        for (int value = 1; value <= 9; value++) {
          cards.add(new Card(value, suit));
        }
      } else {
        for (int value = 1; value <= 4; value++) {
          cards.add(new Card(value));
        }
      }
    }
  }

  public void shuffle() {
    Collections.shuffle(cards);
  }

  public Card drawCard() {
    if (cards.isEmpty()) {
      return null;
    }
    return cards.remove(cards.size() - 1);
  }

  public List<Card> getCards() {
    return new ArrayList<>(cards);
  }

  public boolean isEmpty() {
    return cards.isEmpty();
  }
}
