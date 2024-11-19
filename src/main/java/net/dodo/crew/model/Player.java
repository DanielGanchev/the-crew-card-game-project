package net.dodo.crew.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
  private List<Card> hand = new ArrayList<>();
  private List<Task> tasks = new ArrayList<>();
  private String name;

  public Player(String name) {
    this.name = name;
  }

  public void addCard(Card card) {
    hand.add(card);
  }

  public void addTask(Task task) {
    tasks.add(task);
  }

  public List<Card> getHand() {
    return hand;
  }

  public List<Task> getTasks() {
    return tasks;
  }

  public String getName() {
    return name;
  }
}
