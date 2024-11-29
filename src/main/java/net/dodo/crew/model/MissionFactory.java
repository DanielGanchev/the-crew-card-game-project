package net.dodo.crew.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.dodo.crew.model.enums.MissionCondition;
import net.dodo.crew.model.enums.MissionRestriction;

public class MissionFactory {
  private static final Random random = new Random();

  public static Mission createMission(int missionNumber, int numPlayers) {

    int difficulty = calculateDifficulty(missionNumber);
    Mission mission = new Mission(missionNumber,
        "Mission " + missionNumber + " - Difficulty Level " + difficulty,
        difficulty);


    addSpecialConditions(mission, missionNumber, numPlayers);


    addTimeLimitAndRestrictions(mission, missionNumber);


    List<Task> tasks = createTasks(difficulty, numPlayers);
    for (Task task : tasks) {
      mission.addTask(task);
    }

    return mission;
  }

  private static void addTimeLimitAndRestrictions(Mission mission, int missionNumber) {
    // Add time limits for specific missions
    if (missionNumber % 5 == 0) { // Every 5th mission is timed
      Duration timeLimit;
      MissionCondition alternateCondition;

      switch (mission.getDifficulty()) {
        case 1:
          timeLimit = Duration.ofMinutes(3).plusSeconds(30);
          alternateCondition = MissionCondition.CURRENTS;
          break;
        case 2:
          timeLimit = Duration.ofMinutes(3);
          alternateCondition = MissionCondition.RAPTURE_OF_DEEP;
          break;
        default:
          timeLimit = Duration.ofMinutes(2).plusSeconds(30);
          alternateCondition = MissionCondition.NO_COMMUNICATION;
          break;
      }

      mission.setTimeLimit(timeLimit);
      mission.setCondition(alternateCondition);
    }


    if (mission.getDifficulty() >= 2) {
      MissionRestriction restriction = switch (missionNumber % 8) {
        case 0 -> MissionRestriction.NO_PINK_ROCKET_LEAD;
        case 1 -> MissionRestriction.NO_NINE_PAIR;
        case 2 -> MissionRestriction.NO_WIN_WITH_PINK;
        case 3 -> MissionRestriction.MUST_WIN_HIGHEST;
        case 4 -> MissionRestriction.NO_WIN_WITH_ROCKET;
        case 5 -> MissionRestriction.MUST_FOLLOW_SUIT;
        case 6 -> MissionRestriction.NO_ADJACENT_WINS;
        default -> MissionRestriction.NONE;
      };
      mission.setRestriction(restriction);
    }
  }

  private static int calculateDifficulty(int missionNumber) {
    if (missionNumber <= 10) return 1;
    if (missionNumber <= 20) return 2;
    if (missionNumber <= 30) return 3;
    if (missionNumber <= 40) return 4;
    return 5;
  }

  private static void addSpecialConditions(Mission mission, int missionNumber, int numPlayers) {

    if (missionNumber % 5 == 0) {
      mission.setTimedMission(true);
    }


    if (missionNumber % 7 == 0) {
      mission.setCondition(MissionCondition.DISTRESS_SIGNAL);
    } else if (missionNumber % 11 == 0) {
      mission.setCondition(MissionCondition.CURRENTS);
    } else if (missionNumber % 13 == 0) {
      mission.setCondition(MissionCondition.NO_COMMUNICATION);
    } else if (missionNumber % 17 == 0) {
      mission.setCondition(MissionCondition.RAPTURE_OF_DEEP);
    } else if (missionNumber % 19 == 0) {
      mission.setCondition(MissionCondition.UNFAMILIAR_TERRAIN);
    }
  }

  private static List<Task> createTasks(int difficulty, int numPlayers) {
    List<Task> tasks = new ArrayList<>();
    int numTasks = calculateNumTasks(difficulty, numPlayers);

    // Create a deck to draw task cards from
    Deck taskDeck = new Deck();
    taskDeck.shuffle();

    for (int i = 0; i < numTasks; i++) {
      Card taskCard = taskDeck.drawCard();
      if (taskCard == null) continue;

      Task task = new Task(taskCard);


      if (difficulty >= 3) {

        if (random.nextDouble() < 0.3) {
          task.setOrderNumber(i + 1);
        }
      }

      if (difficulty >= 4) {

        if (random.nextDouble() < 0.2) {
          if (random.nextBoolean()) {
            task.setMustWinWithCard(true);
          } else {
            task.setMustLoseWithCard(true);
          }
        }
      }

      tasks.add(task);
    }

    return tasks;
  }

  private static int calculateNumTasks(int difficulty, int numPlayers) {

    int baseTasks = difficulty + 1;


    int playerBonus = (numPlayers - 3); // 3 is minimum players


    int variation = random.nextInt(2); // 0 or 1

    return Math.min(baseTasks + playerBonus + variation, 8); // Cap at 8 tasks
  }
}

