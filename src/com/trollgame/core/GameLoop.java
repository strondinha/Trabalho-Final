package com.trollgame.core;

import com.trollgame.states.GameStateManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GameLoop - Timer-based game loop
 */
public class GameLoop implements ActionListener {
    private static final int TARGET_FPS = 60;
    private static final int DELAY = 1000 / TARGET_FPS;

    private final Timer timer;
    private final GamePanel gamePanel;
    private final GameStateManager stateManager;

    public GameLoop(GamePanel gamePanel, GameStateManager stateManager) {
        this.gamePanel = gamePanel;
        this.stateManager = stateManager;
        timer = new Timer(DELAY, this);
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gamePanel.update();
        gamePanel.repaint();
    }
}
