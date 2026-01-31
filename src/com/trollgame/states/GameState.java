package com.trollgame.states;

import java.awt.*;

/**
 * GameState - Abstract base class for all game states
 */
public abstract class GameState {
    protected GameStateManager stateManager;

    public GameState(GameStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public abstract void init();
    public abstract void update();
    public abstract void render(Graphics2D g);
    public abstract void keyPressed(int keyCode);
    public abstract void keyReleased(int keyCode);

    public void onCharTyped(char c) { }
    public void mouseMoved(int x, int y) { }
    public void mousePressed(int x, int y) { }

    public void cleanup() { }
}
