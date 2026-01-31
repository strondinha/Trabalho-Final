package com.trollgame.levels;

/**
 * LevelFactory - Creates levels based on level number
 */
public class LevelFactory {

    public static final int TOTAL_LEVELS = 15;

    public static Level createLevel(int levelNumber) {
        Level level;

        switch (levelNumber) {
            case 1: level = new Level1(); break;
            case 2: level = new Level2(); break;
            case 3: level = new Level3(); break;
            case 4: level = new Level4(); break;
            case 5: level = new Level5(); break;
            case 6: level = new Level6(); break;
            case 7: level = new Level7(); break;
            case 8: level = new Level8(); break;
            case 9: level = new Level9(); break;
            case 10: level = new Level10(); break;
            case 11: level = new Level11(); break;
            case 12: level = new Level12(); break;
            case 13: level = new Level13(); break;
            case 14: level = new Level14(); break;
            case 15: level = new Level15(); break;
            default: level = new Level1();
        }

        level.init();
        if (levelNumber == 15) {
            level.expandLevelSpacing(1.30f);
            level.setWorldSize(2000, 600);
        } else {
            level.expandLevelSpacing(1.20f);
            level.setWorldSize(1400, 600);
        }
        int themeIndex = (levelNumber - 1) / 3;
        level.applyTheme(themeIndex);
        return level;
    }

    public static boolean hasNextLevel(int currentLevel) {
        return currentLevel < TOTAL_LEVELS;
    }
}
