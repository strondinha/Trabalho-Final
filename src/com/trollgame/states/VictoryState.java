package com.trollgame.states;

import com.trollgame.core.Game;
import com.trollgame.core.SoundManager;
import com.trollgame.ui.Button;

import java.awt.*;
import java.awt.event.KeyEvent;

public class VictoryState extends GameState {

    private Button menuButton;
    private Button mapButton;
    private Button exitButton;
    private int selectedIndex;

    private float confettiTimer = 0;
    private float[][] confetti;

    private Color backgroundColor = new Color(255, 250, 240);
    private Color titleColor = new Color(255, 180, 80);

    public VictoryState(GameStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void init() {
        int buttonWidth = 140;
        int buttonHeight = 40;
        int gap = 20;
        int totalWidth = buttonWidth * 3 + gap * 2;
        int startX = (Game.WIDTH - totalWidth) / 2;
        int y = 520;

        menuButton = new Button(startX, y, buttonWidth, buttonHeight, "MENU");
        mapButton = new Button(startX + buttonWidth + gap, y, buttonWidth, buttonHeight, "MAPA");
        exitButton = new Button(startX + (buttonWidth + gap) * 2, y, buttonWidth, buttonHeight, "SAIR");

        selectedIndex = 0;
        updateButtonHover();

        SoundManager.stopMusic();
        SoundManager.playSfx("/res/sound/levelup.wav");

        confetti = new float[50][5];
        for (int i = 0; i < confetti.length; i++) {
            resetConfetti(i);
            confetti[i][1] = (float)(Math.random() * Game.HEIGHT);
        }
    }

    private void resetConfetti(int index) {
        confetti[index][0] = (float)(Math.random() * Game.WIDTH);
        confetti[index][1] = -20;
        confetti[index][2] = (float)(Math.random() * 2 - 1);
        confetti[index][3] = (float)(Math.random() * 3 + 2);
        confetti[index][4] = (float)(Math.random() * 4);
    }

    @Override
    public void update() {
        confettiTimer += 0.1f;

        for (int i = 0; i < confetti.length; i++) {
            confetti[i][0] += confetti[i][2];
            confetti[i][1] += confetti[i][3];

            if (confetti[i][1] > Game.HEIGHT + 20) {
                resetConfetti(i);
            }
        }

        menuButton.update();
        mapButton.update();
        exitButton.update();
    }

    @Override
    public void render(Graphics2D g) {
        GradientPaint bg = new GradientPaint(
            0, 0, backgroundColor,
            0, Game.HEIGHT, new Color(255, 235, 220)
        );
        g.setPaint(bg);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        // Vinheta suave
        g.setColor(new Color(0, 0, 0, 30));
        g.fillRect(0, 0, Game.WIDTH, 18);
        g.fillRect(0, Game.HEIGHT - 18, Game.WIDTH, 18);
        g.fillRect(0, 0, 18, Game.HEIGHT);
        g.fillRect(Game.WIDTH - 18, 0, 18, Game.HEIGHT);

        Color[] confettiColors = {
            new Color(255, 200, 100),
            new Color(255, 150, 80),
            new Color(255, 180, 150),
            new Color(255, 220, 180)
        };

        for (float[] c : confetti) {
            g.setColor(confettiColors[(int)c[4]]);
            g.fillRect((int)c[0], (int)c[1], 8, 8);
        }

        g.setColor(new Color(255, 220, 150, 120));
        g.setFont(new Font("Arial", Font.BOLD, 62));
        String title = "VITÓRIA!";
        FontMetrics fm = g.getFontMetrics();
        int titleX = (Game.WIDTH - fm.stringWidth(title)) / 2;
        g.drawString(title, titleX + 3, 133);
        g.setColor(new Color(255, 200, 120, 80));
        g.drawString(title, titleX + 6, 136);

        g.setColor(titleColor);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        g.drawString(title, titleX, 130);

        g.setColor(new Color(180, 120, 60));
        g.setFont(new Font("Arial", Font.ITALIC, 22));
        String sub = "Você venceu todos os desafios!";
        fm = g.getFontMetrics();
        g.drawString(sub, (Game.WIDTH - fm.stringWidth(sub)) / 2, 180);

        g.setColor(new Color(150, 100, 60));
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String msg1 = "Parabéns! Você completou todas as fases";
        String msg2 = "e superou todas as armadilhas.";
        String msg3 = "Seu tempo total foi salvo no ranking.";
        fm = g.getFontMetrics();
        g.drawString(msg1, (Game.WIDTH - fm.stringWidth(msg1)) / 2, 250);
        g.drawString(msg2, (Game.WIDTH - fm.stringWidth(msg2)) / 2, 275);
        g.drawString(msg3, (Game.WIDTH - fm.stringWidth(msg3)) / 2, 310);

        g.setColor(new Color(255, 220, 120));
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String timeText = String.format("TEMPO TOTAL: %.2f s", stateManager.getTotalTime());
        fm = g.getFontMetrics();
        g.drawString(timeText, (Game.WIDTH - fm.stringWidth(timeText)) / 2, 340);

        drawRanking(g);

        menuButton.render(g);
        mapButton.render(g);
        exitButton.render(g);

    }

    private void drawRanking(Graphics2D g) {
        java.util.List<com.trollgame.scores.ScoreManager.Score> scores =
            com.trollgame.scores.ScoreManager.getScores();

        int cx = Game.WIDTH / 2;
        GradientPaint panel = new GradientPaint(
            cx - 220, 350, new Color(0, 0, 0, 160),
            cx + 220, 470, new Color(0, 60, 20, 140)
        );
        g.setPaint(panel);
        g.fillRoundRect(cx - 220, 350, 440, 120, 16, 16);
        g.setColor(new Color(255, 255, 255, 180));
        g.drawRoundRect(cx - 220, 350, 440, 120, 16, 16);

        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.WHITE);
        g.drawString("RANKING", cx - 45, 372);

        g.setFont(new Font("Monospaced", Font.PLAIN, 15));
        g.setColor(new Color(230, 230, 230));
        g.drawString("#   NOME       SKIN   TEMPO", cx - 190, 392);

        int y = 412;
        int limit = Math.min(3, scores.size());
        for (int i = 0; i < limit; i++) {
            com.trollgame.scores.ScoreManager.Score s = scores.get(i);
            if (i == 0) {
                g.setColor(new Color(255, 220, 120));
            } else if (i == 1) {
                g.setColor(new Color(210, 210, 210));
            } else if (i == 2) {
                g.setColor(new Color(205, 160, 120));
            } else {
                g.setColor(new Color(220, 220, 220));
            }
            String line = String.format("%d   %-10s %-5s %.2fs", i + 1, s.name, s.skin, s.time);
            g.drawString(line, cx - 190, y);
            y += 22;
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                selectedIndex = (selectedIndex + 2) % 3;
                updateButtonHover();
                SoundManager.playSfx("/res/sound/click.wav");
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                selectedIndex = (selectedIndex + 1) % 3;
                updateButtonHover();
                SoundManager.playSfx("/res/sound/click.wav");
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                SoundManager.playSfx("/res/sound/levelup.wav");
                if (selectedIndex == 0) {
                    stateManager.setCurrentLevel(1);
                    stateManager.resetTotalTime();
                    stateManager.transitionTo(GameStateManager.MENU_STATE);
                } else if (selectedIndex == 1) {
                    stateManager.transitionTo(GameStateManager.MAP_STATE);
                } else {
                    System.exit(0);
                }
                break;
        }
    }

    @Override
    public void mouseMoved(int x, int y) {
        int before = selectedIndex;
        if (menuButton.contains(x, y)) selectedIndex = 0;
        else if (mapButton.contains(x, y)) selectedIndex = 1;
        else if (exitButton.contains(x, y)) selectedIndex = 2;

        if (before != selectedIndex) {
            SoundManager.playSfx("/res/sound/click.wav");
            updateButtonHover();
        }
    }

    @Override
    public void mousePressed(int x, int y) {
        if (menuButton.contains(x, y)) {
            SoundManager.playSfx("/res/sound/levelup.wav");
            stateManager.setCurrentLevel(1);
            stateManager.resetTotalTime();
            stateManager.transitionTo(GameStateManager.MENU_STATE);
        } else if (mapButton.contains(x, y)) {
            SoundManager.playSfx("/res/sound/levelup.wav");
            stateManager.transitionTo(GameStateManager.MAP_STATE);
        } else if (exitButton.contains(x, y)) {
            SoundManager.playSfx("/res/sound/levelup.wav");
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(int keyCode) { }

    private void updateButtonHover() {
        menuButton.setHovered(selectedIndex == 0);
        mapButton.setHovered(selectedIndex == 1);
        exitButton.setHovered(selectedIndex == 2);
    }
}
