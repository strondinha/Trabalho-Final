package com.trollgame.states;

import com.trollgame.levels.LevelFactory;
import java.awt.event.*;

/**
 * GameStateManager - Manages game states and transitions
 */
public class GameStateManager implements KeyListener, MouseListener, MouseMotionListener {
    private GameState currentState;
    private int currentLevel;

    public static final int LOGIN_STATE = 0;
    public static final int MENU_STATE = 1;
    public static final int MAP_STATE = 2;
    public static final int PLAY_STATE = 3;
    public static final int TRANSITION_STATE = 4;
    public static final int GAMEOVER_STATE = 5;
    public static final int VICTORY_STATE = 6;
    public static final int INSTRUCTIONS_STATE = 7;

    private String playerName = "";
    private int maxUnlockedLevel = 1;
    private int selectedSkin = 0;
    private double totalTime = 0;

    public GameStateManager() {
        currentLevel = 1;
        setState(LOGIN_STATE);
    }

    public void setState(int stateId) {
        if (currentState != null) {
            currentState.cleanup();
        }

        switch (stateId) {
            case LOGIN_STATE:
                currentState = new LoginState(this);
                break;
            case MENU_STATE:
                currentState = new MenuState(this);
                break;
            case MAP_STATE:
                currentState = new MapState(this);
                break;
            case PLAY_STATE:
                currentState = new PlayState(this);
                break;
            case TRANSITION_STATE:
                currentState = new TransitionState(this);
                break;
            case GAMEOVER_STATE:
                currentState = new GameOverState(this);
                break;
            case VICTORY_STATE:
                currentState = new VictoryState(this);
                break;
            case INSTRUCTIONS_STATE:
                currentState = new InstructionsState(this);
                break;
        }

        currentState.init();
    }

    public void transitionTo(int targetState) {
        if (currentState instanceof TransitionState) {
            return;
        }
        TransitionState transition = new TransitionState(this);
        transition.setTargetState(targetState);
        currentState.cleanup();
        currentState = transition;
        currentState.init();
    }

    public void update() { currentState.update(); }
    public void render(java.awt.Graphics2D g) { currentState.render(g); }

    public int getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(int level) { this.currentLevel = level; }
    public void nextLevel() { currentLevel++; }

    public int getMaxUnlockedLevel() { return maxUnlockedLevel; }
    public void unlockLevel(int level) {
        maxUnlockedLevel = Math.min(Math.max(maxUnlockedLevel, level), LevelFactory.TOTAL_LEVELS);
    }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String name) { this.playerName = name; }

    public int getSelectedSkin() { return selectedSkin; }
    public void nextSkin(int maxSkins) { selectedSkin = (selectedSkin + 1) % maxSkins; }
    public void previousSkin(int maxSkins) { selectedSkin = (selectedSkin - 1 + maxSkins) % maxSkins; }

    public double getTotalTime() { return totalTime; }
    public void addToTotalTime(double time) { totalTime += time; }
    public void resetTotalTime() { totalTime = 0; }

    public KeyListener getKeyListener() { return this; }

    @Override
    public void keyPressed(KeyEvent e) { currentState.keyPressed(e.getKeyCode()); }

    @Override
    public void keyReleased(KeyEvent e) { currentState.keyReleased(e.getKeyCode()); }

    @Override
    public void keyTyped(KeyEvent e) { currentState.onCharTyped(e.getKeyChar()); }

    @Override
    public void mousePressed(MouseEvent e) { currentState.mousePressed(e.getX(), e.getY()); }

    @Override
    public void mouseMoved(MouseEvent e) { currentState.mouseMoved(e.getX(), e.getY()); }

    @Override
    public void mouseDragged(MouseEvent e) { currentState.mouseMoved(e.getX(), e.getY()); }

    @Override public void mouseClicked(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }
}
