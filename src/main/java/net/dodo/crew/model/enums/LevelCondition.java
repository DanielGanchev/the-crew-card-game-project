package net.dodo.crew.model.enums;

public enum LevelCondition {
  NORMAL,
  RAPTURE_OF_THE_DEEP,
  CURRENTS,
  UNFAMILIAR_TERRAIN,
  FREE_TASK_SELECTION,
  TIME_LIMIT,
  ALL_TASKS_TO_CAPTAIN;


  private  int timeLimit;

  LevelCondition() {
    this(0);
  }


  LevelCondition(int timeLimit) {
    this.timeLimit = timeLimit;
  }

  public int getTimeLimit() {
    return timeLimit;
  }

  public LevelCondition setTimeLimit(int timeLimit) {
    this.timeLimit = timeLimit;
    return this;
  }
}
