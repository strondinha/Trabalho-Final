package com.trollgame.ui;

import com.trollgame.core.Game;
import com.trollgame.levels.LevelFactory;
import java.awt.*;

/**
 * HUD - Heads Up Display
 */
public class HUD {
    private int currentLevel;
    private int deathCount;
    private int displayedDeaths;
    private String playerName = "";
    private double levelTime;

    private Color panelColor = new Color(50, 40, 35, 180);
    private Color panelBorder = new Color(255, 200, 150, 100);
    private Color textColor = new Color(255, 248, 230);
    private Color accentColor = new Color(255, 180, 100);
    private Color deathColor = new Color(255, 100, 100);

    private Font levelFont = new Font("Arial", Font.BOLD, 18);
    private Font deathFont = new Font("Arial", Font.BOLD, 16);
    private Font labelFont = new Font("Arial", Font.PLAIN, 12);
    private Font hintFont = new Font("Arial", Font.PLAIN, 14);

    private float deathShake = 0;
    private float levelPulse = 0;

    public HUD() {
        this.currentLevel = 1;
        this.deathCount = 0;
        this.displayedDeaths = 0;
    }

    public void update() {
        if (displayedDeaths < deathCount) {
            displayedDeaths++;
            deathShake = 10;
        }

        if (deathShake > 0) {
            deathShake *= 0.85f;
        }

        levelPulse += 0.05f;
    }

    public void render(Graphics2D g) {
        drawLevelPanel(g);
        drawTimePanel(g);
        drawDeathPanel(g);
        drawNameBanner(g);
        drawHintPanel(g);
    }

    private void drawLevelPanel(Graphics2D g) {
        int panelX = 15;
        int panelY = 15;
        int panelWidth = 120;
        int panelHeight = 50;

        g.setColor(panelColor);
        g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 15, 15);

        g.setColor(panelBorder);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 15, 15);

        g.setColor(accentColor);
        g.setFont(labelFont);
        g.drawString("FASE", panelX + 15, panelY + 18);

        float pulse = (float)(1 + Math.sin(levelPulse) * 0.05f);
        Font pulsedFont = levelFont.deriveFont(levelFont.getSize() * pulse);
        g.setFont(pulsedFont);
        g.setColor(textColor);

        String levelText = String.format("%d / %d", currentLevel, LevelFactory.TOTAL_LEVELS);
        g.drawString(levelText, panelX + 15, panelY + 40);

        int barX = panelX + 70;
        int barY = panelY + 25;
        int barWidth = 40;
        int barHeight = 8;

        g.setColor(new Color(30, 25, 20));
        g.fillRoundRect(barX, barY, barWidth, barHeight, 4, 4);

        int progress = (int)(barWidth * (currentLevel / (float)LevelFactory.TOTAL_LEVELS));
        GradientPaint gradient = new GradientPaint(barX, barY, accentColor, barX + progress, barY, new Color(255, 220, 150));
        g.setPaint(gradient);
        g.fillRoundRect(barX, barY, progress, barHeight, 4, 4);
    }

    private void drawDeathPanel(Graphics2D g) {
        int panelX = Game.WIDTH - 135;
        int panelY = 15;
        int panelWidth = 120;
        int panelHeight = 50;

        float shakeX = (float)(Math.random() - 0.5) * deathShake;
        float shakeY = (float)(Math.random() - 0.5) * deathShake;
        panelX += (int)shakeX;
        panelY += (int)shakeY;

        g.setColor(panelColor);
        g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 15, 15);

        g.setColor(panelBorder);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 15, 15);

        g.setColor(deathColor);
        g.fillOval(panelX + 12, panelY + 15, 20, 20);
        g.setColor(panelColor);
        g.fillOval(panelX + 15, panelY + 20, 5, 5);
        g.fillOval(panelX + 24, panelY + 20, 5, 5);
        g.fillRect(panelX + 18, panelY + 28, 8, 4);

        g.setColor(deathColor);
        g.setFont(labelFont);
        g.drawString("MORTES", panelX + 40, panelY + 18);

        g.setFont(deathFont);
        g.setColor(textColor);
        g.drawString(String.valueOf(displayedDeaths), panelX + 40, panelY + 38);
    }

    private void drawTimePanel(Graphics2D g) {
        int panelX = Game.WIDTH - 135;
        int panelY = 75;
        int panelWidth = 120;
        int panelHeight = 50;

        g.setColor(panelColor);
        g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 15, 15);

        g.setColor(panelBorder);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 15, 15);

        g.setColor(accentColor);
        g.setFont(labelFont);
        g.drawString("TEMPO", panelX + 40, panelY + 18);

        g.setFont(deathFont);
        g.setColor(textColor);
        g.drawString(formatTime(levelTime), panelX + 30, panelY + 38);
    }

    private void drawNameBanner(Graphics2D g) {
        if (playerName == null || playerName.isBlank()) return;

        String label = "JOGADOR: " + playerName;
        g.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth(label) + 30;
        int height = 26;
        int x = (Game.WIDTH - width) / 2;
        int y = 12;

        g.setColor(new Color(20, 20, 20, 150));
        g.fillRoundRect(x, y, width, height, 12, 12);
        g.setColor(new Color(255, 220, 160, 180));
        g.drawRoundRect(x, y, width, height, 12, 12);

        g.setColor(new Color(255, 240, 210));
        g.drawString(label, x + 15, y + 18);
    }

    private void drawHintPanel(Graphics2D g) {
        String hint = getHintForLevel(currentLevel);
        if (hint == null || hint.isBlank()) return;

        String[] lines = hint.split("\\|");
        g.setFont(hintFont);
        FontMetrics fm = g.getFontMetrics();
        int maxWidth = 0;
        for (String line : lines) {
            maxWidth = Math.max(maxWidth, fm.stringWidth(line.trim()));
        }

        int paddingX = 16;
        int paddingY = 10;
        int lineHeight = fm.getHeight();
        int panelWidth = maxWidth + paddingX * 2;
        int panelHeight = lineHeight * lines.length + paddingY * 2 - 6;
        int x = (Game.WIDTH - panelWidth) / 2;
        int y = Game.HEIGHT - panelHeight - 14;

        g.setColor(new Color(20, 20, 20, 140));
        g.fillRoundRect(x, y, panelWidth, panelHeight, 14, 14);
        g.setColor(new Color(255, 220, 160, 160));
        g.drawRoundRect(x, y, panelWidth, panelHeight, 14, 14);

        g.setColor(new Color(255, 245, 230));
        int textY = y + paddingY + fm.getAscent();
        for (String line : lines) {
            String trimmed = line.trim();
            int textX = x + (panelWidth - fm.stringWidth(trimmed)) / 2;
            g.drawString(trimmed, textX, textY);
            textY += lineHeight;
        }
    }

    private String getHintForLevel(int level) {
        switch (level) {
            case 1: return "Pule cedo e teste o chão.";
            case 2: return "Cuidado com o buraco oculto.";
            case 3: return "Plataforma rápida = salto curto.";
            case 4: return "O teto pode descer, avance.";
            case 5: return "A gravidade pode virar.|Pule antes.";
            case 6: return "Algumas plataformas somem.|Não hesite.";
            case 7: return "Ajuste o timing da móvel.";
            case 8: return "Evite o teto surpresa.";
            case 9: return "Use a plataforma móvel|como apoio.";
            case 10: return "Passe rápido pelo centro.";
            case 11: return "Cuidado com o teto|e a móvel rápida.";
            case 12: return "As três somem em sequência.";
            case 13: return "Pule antes do sumiço.";
            case 14: return "O teto desce mais lento,|mas é traiçoeiro.";
            case 15: return "Timing é tudo. Calma.";
            default: return "";
        }
    }

    private String formatTime(double seconds) {
        int total = (int)Math.floor(seconds);
        int min = total / 60;
        int sec = total % 60;
        return String.format("%02d:%02d", min, sec);
    }

    public void setCurrentLevel(int level) { this.currentLevel = level; }
    public void incrementDeaths() { deathCount++; }
    public int getDeathCount() { return deathCount; }
    public void setPlayerName(String name) { this.playerName = name; }
    public void setLevelTime(double levelTime) { this.levelTime = levelTime; }

    public void reset() {
        deathCount = 0;
        displayedDeaths = 0;
        deathShake = 0;
    }
}
