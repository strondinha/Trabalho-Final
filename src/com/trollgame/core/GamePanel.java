package com.trollgame.core;

import com.trollgame.states.GameStateManager;

import javax.swing.*;
import java.awt.*;

/**
 * GamePanel - Main rendering surface and game loop host
 */
public class GamePanel extends JPanel {
    private GameStateManager stateManager;
    private GameLoop gameLoop;

    public GamePanel() {
        setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        setBackground(new Color(255, 248, 220));
        setFocusable(true);
        setDoubleBuffered(true);

        stateManager = new GameStateManager();
        addKeyListener(stateManager.getKeyListener());
        addMouseListener(stateManager);
        addMouseMotionListener(stateManager);
    }

    public void startGame() {
        gameLoop = new GameLoop(this, stateManager);
        gameLoop.start();
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        stateManager.render(g2d);
    }

    public void update() {
        stateManager.update();
    }
}
