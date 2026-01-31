package com.trollgame.states;

import com.trollgame.assets.Assets;
import com.trollgame.core.Game;
import com.trollgame.core.SoundManager;
import com.trollgame.ui.Button;
import com.trollgame.ui.MatrixBackground;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {

    private Button[] buttons;
    private int selectedIndex;
    private MatrixBackground matrixBg;

    public MenuState(GameStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void init() {
        matrixBg = new MatrixBackground();

        SoundManager.loopMusicWithFallback("/res/sound/theme_4.wav", "/res/sound/theme_0.wav");

        int buttonWidth = 240;
        int buttonHeight = 50;
        int centerX = Game.WIDTH / 2 - buttonWidth / 2;

        buttons = new Button[] {
            new Button(centerX, 310, buttonWidth, buttonHeight, "JOGAR"),
            new Button(centerX, 380, buttonWidth, buttonHeight, "SKIN"),
            new Button(centerX, 450, buttonWidth, buttonHeight, "INSTRUÇÕES"),
            new Button(centerX, 520, buttonWidth, buttonHeight, "SAIR")
        };

        selectedIndex = 0;
        updateButtonHover();
    }

    @Override
    public void update() {
        matrixBg.update();
        for (Button b : buttons) b.update();
    }

    @Override
    public void render(Graphics2D g) {
        matrixBg.render(g);

        // Vinheta suave para foco
        g.setColor(new Color(0, 0, 0, 90));
        g.fillRect(0, 0, Game.WIDTH, 16);
        g.fillRect(0, Game.HEIGHT - 16, Game.WIDTH, 16);
        g.fillRect(0, 0, 16, Game.HEIGHT);
        g.fillRect(Game.WIDTH - 16, 0, 16, Game.HEIGHT);

        g.setColor(new Color(0, 120, 0));
        g.setFont(new Font("Impact", Font.BOLD, 90));
        String title = "TROLLBOUND";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (Game.WIDTH - fm.stringWidth(title)) / 2 + 3, 145);

        g.setColor(new Color(0, 80, 0, 120));
        g.drawString(title, (Game.WIDTH - fm.stringWidth(title)) / 2 + 6, 148);

        g.setColor(Color.GREEN);
        g.drawString(title, (Game.WIDTH - fm.stringWidth(title)) / 2, 140);

        for (Button b : buttons) b.render(g);

        drawSkinCard(g);

    }

    @Override
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                selectedIndex = (selectedIndex + buttons.length - 1) % buttons.length;
                updateButtonHover();
                SoundManager.playSfx("/res/sound/click.wav");
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                selectedIndex = (selectedIndex + 1) % buttons.length;
                updateButtonHover();
                SoundManager.playSfx("/res/sound/click.wav");
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                stateManager.previousSkin(Assets.SKIN_NAMES.length);
                SoundManager.playSfx("/res/sound/click.wav");
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                stateManager.nextSkin(Assets.SKIN_NAMES.length);
                SoundManager.playSfx("/res/sound/click.wav");
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                executeSelected();
                SoundManager.playSfx("/res/sound/levelup.wav");
                break;
        }
    }

    @Override
    public void keyReleased(int keyCode) { }

    @Override
    public void mouseMoved(int x, int y) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].contains(x, y)) {
                selectedIndex = i;
                updateButtonHover();
                SoundManager.playSfx("/res/sound/click.wav");
                break;
            }
        }
    }

    @Override
    public void mousePressed(int x, int y) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].contains(x, y)) {
                selectedIndex = i;
                executeSelected();
                SoundManager.playSfx("/res/sound/levelup.wav");
                break;
            }
        }
    }

    private void executeSelected() {
        switch (selectedIndex) {
            case 0:
                stateManager.resetTotalTime();
                stateManager.setCurrentLevel(1);
                stateManager.transitionTo(GameStateManager.MAP_STATE);
                break;
            case 1: stateManager.nextSkin(Assets.SKIN_NAMES.length); break;
            case 2: stateManager.setState(GameStateManager.INSTRUCTIONS_STATE); break;
            case 3: System.exit(0); break;
        }
    }

    private void updateButtonHover() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setHovered(i == selectedIndex);
        }
    }

    private void drawSkinCard(Graphics2D g) {
        int cardWidth = 240;
        int cardHeight = 80;
        int x = Game.WIDTH / 2 - cardWidth / 2;
        int y = 190;

        g.setColor(new Color(20, 15, 10, 180));
        g.fillRoundRect(x, y, cardWidth, cardHeight, 16, 16);
        g.setColor(new Color(255, 180, 120, 180));
        g.drawRoundRect(x, y, cardWidth, cardHeight, 16, 16);

        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.setColor(new Color(255, 230, 200));
        g.drawString("SKIN ATUAL", x + 12, y + 22);

        int skin = stateManager.getSelectedSkin();
        String skinName = Assets.SKIN_NAMES[skin];
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(new Color(255, 245, 230));
        g.drawString(skinName, x + 12, y + 52);

        g.setColor(new Color(255, 200, 150));
        int arrowY = y + 35;
        g.fillPolygon(new int[]{x + 200, x + 215, x + 215}, new int[]{arrowY, arrowY - 8, arrowY + 8}, 3);
        g.fillPolygon(new int[]{x + 225, x + 210, x + 210}, new int[]{arrowY, arrowY - 8, arrowY + 8}, 3);

        if (Assets.getSkin(skin) != null) {
            g.drawImage(Assets.getSkin(skin), x + 160, y + 20, 40, 40, null);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(new Color(230, 210, 190));
        g.drawString("A/D ou ←/→ para trocar", x + 12, y + 70);
    }
}
