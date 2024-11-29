package net.dodo.crew.model.enums;

public enum MissionCondition {
  NONE("Standard mission"),
  DISTRESS_SIGNAL("Distress Signal: Pass a non-submarine card to adjacent member before first trick"),
  CURRENTS("Currents: Use a card to communicate but no Sonar token"),
  NO_COMMUNICATION("No communication allowed"),
  RAPTURE_OF_DEEP("Rapture of the Deep: Use fewer Sonar tokens"),
  UNFAMILIAR_TERRAIN("Unfamiliar Terrain: Random communication rules"),
  FREE_TASK_SELECTION("Free Task Selection: Discuss and assign tasks");

  private final String description;

  MissionCondition(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}

