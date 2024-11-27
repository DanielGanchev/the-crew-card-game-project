package net.dodo.crew.model;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.dodo.crew.model.enums.LevelCondition;

public class Level {
  private int levelNumber;
  private int difficulty;
  private int maxAttempts = 10; // Default max attempts
  private List<Task> drawnTasks;
  private Set<LevelCondition> conditions;



  public Level(int levelNumber, int difficulty, List<Task> drawnTasks, Set<LevelCondition> conditions) {
    this.levelNumber = levelNumber;
    this.difficulty = difficulty;
    this.drawnTasks = drawnTasks;
    this.conditions = conditions;
  }


  public int getLevelNumber() {
    return levelNumber;
  }

  public Level setLevelNumber(int levelNumber) {
    this.levelNumber = levelNumber;
    return this;
  }

  public int getDifficulty() {
    return difficulty;
  }

  public Level setDifficulty(int difficulty) {
    this.difficulty = difficulty;
    return this;
  }

  public int getMaxAttempts() {
    return maxAttempts;
  }

  public Level setMaxAttempts(int maxAttempts) {
    this.maxAttempts = maxAttempts;
    return this;
  }

  public List<Task> getDrawnTasks() {
    return drawnTasks;
  }

  public Level setDrawnTasks(List<Task> drawnTasks) {
    this.drawnTasks = drawnTasks;
    return this;
  }

  public Set<LevelCondition> getConditions() {
    return conditions;
  }

  public Level setConditions(Set<LevelCondition> conditions) {
    this.conditions = conditions;
    return this;
  }

  public int getAttempts() {
    return attempts;
  }

  public Level setAttempts(int attempts) {
    this.attempts = attempts;
    return this;
  }

  public int getCurrentAttempts() {
    return attempts;
  }


  private int attempts = 0; // Track attempts


  public void incrementAttempts() {
    attempts++;
  }

  public boolean isFailed() {
    return attempts >= maxAttempts;
  }




  public static Map<Integer, Level> getAllLevels() {
    Map<Integer, Level> levels = new HashMap<>();

    // Level 1
    List<Task> level1Tasks = new ArrayList<>();
    // ... add tasks for level 1
    Set<LevelCondition> level1Conditions = EnumSet.of(LevelCondition.CURRENTS);
    levels.put(1, new Level(1, 1, level1Tasks, level1Conditions));


    // Level 2  (Example with multiple conditions)
    List<Task> level2Tasks = new ArrayList<>(); // ... add tasks
    Set<LevelCondition> level2Conditions = EnumSet.of(
        LevelCondition.RAPTURE_OF_THE_DEEP,
        LevelCondition.TIME_LIMIT
    );
        // 300 seconds time limit

    levels.put(2, new Level(2, 2, level2Tasks, level2Conditions));


    // ... Add other levels...

    return levels;

  }




  public void applyConditions(Game game) {
    for (LevelCondition condition : conditions) {
      switch (condition) {
        case CURRENTS:
          game.applyCurrentsRule();
          break;
        case RAPTURE_OF_THE_DEEP:
          game.applyRaptureRule();
          break;
        case ALL_TASKS_TO_CAPTAIN:
          game.assignAllTasksToCaptain();
          break;
        case TIME_LIMIT:


          if (levelNumber == 14 || levelNumber == 15 || levelNumber == 16 || levelNumber == 26) {

            int timeLimit = getTimeLimitForLevel(levelNumber);
            game.setTimeLimit(timeLimit);


          }
          break;


        default:


      }
    }
  }
  private int getTimeLimitForLevel(int levelNumber) {
    switch (levelNumber) {
      case 14: return 210;
      case 15: return 180;
      case 16: return 150;
      case 26: return 300;
      default: return 0;
    }
  }
}
