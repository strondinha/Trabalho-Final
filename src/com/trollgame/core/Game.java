package com.trollgame.core;

import javax.swing.*;

/**
 * Main game class - Entry point and window management
 */
public class Game {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "Trollbound";

    private JFrame frame;
    private GamePanel gamePanel;

    public Game() {
        initializeWindow();
    }

    private void initializeWindow() {
        frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gamePanel.startGame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }
}
