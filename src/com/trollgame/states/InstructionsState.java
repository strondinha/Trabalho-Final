package com.trollgame.states;

import com.trollgame.core.Game;
import com.trollgame.ui.MatrixBackground;

import java.awt.*;
import java.awt.event.KeyEvent;

public class InstructionsState extends GameState {

    private MatrixBackground matrixBg;
    private Rectangle backBtn;

    public InstructionsState(GameStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void init() {
        matrixBg = new MatrixBackground();
        backBtn = new Rectangle(Game.WIDTH / 2 - 100, 500, 200, 50);
    }

    @Override
    public void update() {
        matrixBg.update();
    }

    @Override
    public void render(Graphics2D g) {
        matrixBg.render(g);

        g.setColor(new Color(0, 0, 0, 220));
        g.fillRect(80, 80, Game.WIDTH - 160, Game.HEIGHT - 160);
        g.setColor(Color.GREEN);
        g.drawRect(80, 80, Game.WIDTH - 160, Game.HEIGHT - 160);

        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.setColor(Color.WHITE);
        g.drawString("COMO JOGAR", Game.WIDTH / 2 - 120, 150);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Setas/WASD para mover", Game.WIDTH / 2 - 150, 220);
        g.drawString("Espaço para pular", Game.WIDTH / 2 - 150, 260);
        g.drawString("ESC para pausar", Game.WIDTH / 2 - 150, 300);

        g.setColor(Color.GRAY);
        g.fillRoundRect(backBtn.x, backBtn.y, backBtn.width, backBtn.height, 10, 10);
        g.setColor(Color.WHITE);
        g.drawRoundRect(backBtn.x, backBtn.y, backBtn.width, backBtn.height, 10, 10);
        g.drawString("VOLTAR", backBtn.x + 60, backBtn.y + 32);
    }

    @Override
    public void keyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_ESCAPE) {
            stateManager.setState(GameStateManager.MENU_STATE);
        }
    }

    @Override
    public void keyReleased(int keyCode) { }

    @Override
    public void mousePressed(int x, int y) {
        if (backBtn.contains(x, y)) {
            stateManager.setState(GameStateManager.MENU_STATE);
        }
    }
}
