package com.trollgame.states;

import com.trollgame.core.Game;
import com.trollgame.ui.MatrixBackground;

import java.awt.*;
import java.awt.event.KeyEvent;

public class LoginState extends GameState {

    private MatrixBackground matrixBg;
    private StringBuilder name = new StringBuilder();
    private boolean error;

    public LoginState(GameStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void init() {
        matrixBg = new MatrixBackground();
        name.setLength(0);
        error = false;
    }

    @Override
    public void update() {
        matrixBg.update();
    }

    @Override
    public void render(Graphics2D g) {
        matrixBg.render(g);

        int w = Game.WIDTH;
        int h = Game.HEIGHT;

        g.setColor(new Color(0, 50, 0, 200));
        g.fillRoundRect(w / 2 - 200, h / 2 - 100, 400, 200, 20, 20);
        g.setColor(Color.GREEN);
        g.drawRoundRect(w / 2 - 200, h / 2 - 100, 400, 200, 20, 20);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.WHITE);
        g.drawString("LOGIN", w / 2 - 50, h / 2 - 50);

        g.setFont(new Font("Monospaced", Font.BOLD, 28));
        String cursor = (System.currentTimeMillis() / 400) % 2 == 0 ? "_" : " ";
        g.drawString(name.toString() + cursor, w / 2 - 150, h / 2 + 20);

        if (error) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("Digite um nome válido", w / 2 - 90, h / 2 + 55);
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_BACK_SPACE && name.length() > 0) {
            name.deleteCharAt(name.length() - 1);
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            if (name.toString().trim().length() > 0) {
                stateManager.setPlayerName(name.toString().trim());
                stateManager.setState(GameStateManager.MENU_STATE);
            } else {
                error = true;
            }
        }
    }

    @Override
    public void keyReleased(int keyCode) { }

    @Override
    public void onCharTyped(char c) {
        if (Character.isLetterOrDigit(c) && name.length() < 12) {
            name.append(c);
            error = false;
        }
    }
}
