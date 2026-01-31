package com.trollgame.states;

import com.trollgame.core.Game;
import com.trollgame.ui.Button;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverState extends GameState {

    private Button retryButton;
    private Button menuButton;
    private int selectedIndex;

    private Color backgroundColor = new Color(60, 40, 40);
    private Color textColor = new Color(255, 200, 180);

    public GameOverState(GameStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void init() {
        int buttonWidth = 200;
        int buttonHeight = 50;
        int centerX = Game.WIDTH / 2 - buttonWidth / 2;

        retryButton = new Button(centerX, 320, buttonWidth, buttonHeight, "REINICIAR");
        menuButton = new Button(centerX, 400, buttonWidth, buttonHeight, "MENU");

        selectedIndex = 0;
        updateButtonHover();
    }

    @Override
    public void update() {
        retryButton.update();
        menuButton.update();
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        g.setColor(new Color(220, 80, 60));
        g.setFont(new Font("Arial", Font.BOLD, 56));
        String title = "FIM DE JOGO";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (Game.WIDTH - fm.stringWidth(title)) / 2, 180);

        g.setColor(textColor);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String sub = "A pegadinha venceu desta vez...";
        fm = g.getFontMetrics();
        g.drawString(sub, (Game.WIDTH - fm.stringWidth(sub)) / 2, 230);

        retryButton.render(g);
        menuButton.render(g);

    }

    @Override
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                selectedIndex = 0;
                updateButtonHover();
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                selectedIndex = 1;
                updateButtonHover();
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                if (selectedIndex == 0) {
                    stateManager.transitionTo(GameStateManager.PLAY_STATE);
                } else {
                    stateManager.setCurrentLevel(1);
                    stateManager.resetTotalTime();
                    stateManager.transitionTo(GameStateManager.MENU_STATE);
                }
                break;
        }
    }

    @Override
    public void keyReleased(int keyCode) { }

    private void updateButtonHover() {
        retryButton.setHovered(selectedIndex == 0);
        menuButton.setHovered(selectedIndex == 1);
    }
}
