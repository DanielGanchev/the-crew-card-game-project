package net.dodo.crew.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
  private List<Card> hand = new ArrayList<>();
  private List<Task> tasks = new ArrayList<>();
  private String name;
  private boolean distressSignal = true;

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

  public Player setHand(List<Card> hand) {
    this.hand = hand;
    return this;
  }

  public Player setTasks(List<Task> tasks) {
    this.tasks = tasks;
    return this;
  }

  public Player setName(String name) {
    this.name = name;
    return this;
  }

  public boolean isDistressSignal() {
    return distressSignal;
  }

  public Player setDistressSignal(boolean distressSignal) {
    this.distressSignal = distressSignal;
    return this;
  }
}
