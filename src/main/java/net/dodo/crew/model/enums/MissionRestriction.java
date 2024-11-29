package net.dodo.crew.model.enums;

public enum MissionRestriction {
  NONE("No special restrictions"),
  NO_PINK_ROCKET_LEAD("Cannot open a trick with pink or rocket cards"),
  NO_NINE_PAIR("Never two 9s more than other crew"),
  NO_WIN_WITH_PINK("Cannot win tricks with pink cards"),
  MUST_WIN_HIGHEST("Must win with highest card of the suit"),
  NO_WIN_WITH_ROCKET("Cannot win tricks with rocket cards"),
  MUST_FOLLOW_SUIT("Must follow suit if possible"),
  NO_ADJACENT_WINS("Cannot win two tricks in a row");

  private final String description;

  MissionRestriction(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}

