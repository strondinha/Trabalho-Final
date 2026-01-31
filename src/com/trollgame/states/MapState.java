package com.trollgame.states;

import com.trollgame.core.Game;
import com.trollgame.levels.LevelFactory;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MapState extends GameState {

    private int selectedLevel;
    private Rectangle[] nodes;
    private Point[] coords;
    private Rectangle backBtn;
    private float anim;
    private float pulse;
    private int themeIndex;

    public MapState(GameStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void init() {
        selectedLevel = Math.min(stateManager.getCurrentLevel(), LevelFactory.TOTAL_LEVELS);
        themeIndex = (selectedLevel - 1) / 3;
        buildMap();
    }

    private void buildMap() {
        int total = LevelFactory.TOTAL_LEVELS;
        nodes = new Rectangle[total + 1];
        coords = new Point[total + 1];

        int cols = 3;
        int startX = 140;
        int startY = Game.HEIGHT - 100;
        int dx = 220;
        int dy = 95;
        int[] rowOffsetX = {0, 30, -20, 40, -30, 20};

        for (int i = 1; i <= total; i++) {
            int row = (i - 1) / cols;
            int col = (i - 1) % cols;
            int baseX = (row % 2 == 0) ? startX + col * dx : startX + (cols - 1 - col) * dx;
            int x = baseX + rowOffsetX[row % rowOffsetX.length] + (int)(Math.sin(i * 0.7) * 12);
            int y = startY - row * dy - (int)(Math.cos(i * 0.6) * 8);

            coords[i] = new Point(x, y);
            nodes[i] = new Rectangle(x - 22, y - 22, 44, 44);
        }

        backBtn = new Rectangle(20, 20, 120, 40);
    }

    @Override
    public void update() {
        anim += 0.02f;
        pulse += 0.05f;
        themeIndex = (selectedLevel - 1) / 3;
    }

    @Override
    public void render(Graphics2D g) {
        Color[] theme = getThemePalette();
        GradientPaint bg = new GradientPaint(
            0, 0, theme[0],
            0, Game.HEIGHT, theme[1]
        );
        g.setPaint(bg);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        g.setColor(new Color(255, 255, 255, 90));
        for (int i = 0; i < 120; i++) {
            int x = (int)(Math.sin(anim + i * 0.3) * 260 + (i * 41 % Game.WIDTH));
            int y = (i * 47) % Game.HEIGHT;
            g.fillRect((x + Game.WIDTH) % Game.WIDTH, y, (i % 3 == 0 ? 2 : 1), (i % 3 == 0 ? 2 : 1));
        }

        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.setColor(new Color(240, 230, 200));
        g.drawString("MAPA DO TESOURO", Game.WIDTH / 2 - 130, 45);

        float[] dash = {10f, 10f};
        g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10f, dash, (float)(anim * 20 % 20)));
        for (int i = 1; i < LevelFactory.TOTAL_LEVELS; i++) {
            boolean unlocked = i < stateManager.getMaxUnlockedLevel();
            g.setColor(unlocked ? theme[2] : new Color(80, 80, 100));
            g.drawLine(coords[i].x, coords[i].y, coords[i + 1].x, coords[i + 1].y);
        }

        for (int i = 1; i <= LevelFactory.TOTAL_LEVELS; i++) {
            boolean unlocked = i <= stateManager.getMaxUnlockedLevel();
            int radius = (i == selectedLevel) ? 24 : 20;

            if (i == selectedLevel) {
                float glow = (float)(0.6 + Math.sin(pulse) * 0.4);
                g.setColor(new Color(255, 200, 120, (int)(120 * glow)));
                g.fillOval(coords[i].x - 30, coords[i].y - 30, 60, 60);
            }

            if (unlocked) {
                GradientPaint node = new GradientPaint(
                    coords[i].x - radius, coords[i].y - radius, theme[3],
                    coords[i].x + radius, coords[i].y + radius, theme[4]
                );
                g.setPaint(node);
            } else {
                g.setColor(new Color(80, 80, 100));
            }
            g.fillOval(coords[i].x - radius, coords[i].y - radius, radius * 2, radius * 2);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("" + i, coords[i].x - 4, coords[i].y + 4);
        }

        drawArrow(g);

        g.setColor(new Color(40, 40, 60, 200));
        g.fillRoundRect(backBtn.x, backBtn.y, backBtn.width, backBtn.height, 10, 10);
        g.setColor(Color.WHITE);
        g.drawRoundRect(backBtn.x, backBtn.y, backBtn.width, backBtn.height, 10, 10);
        g.drawString("MENU", backBtn.x + 35, backBtn.y + 25);

        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(new Color(200, 190, 160));
        g.drawString("Pistas pontilhadas ligam as fases", 20, Game.HEIGHT - 15);
    }

    private void drawArrow(Graphics2D g) {
        Point p = coords[selectedLevel];
        if (p == null) return;
        int arrowY = p.y - 40 - (int)(Math.sin(pulse) * 6);
        int arrowX = p.x;
        g.setColor(new Color(255, 220, 140));
        int[] xPoints = {arrowX, arrowX - 10, arrowX + 10};
        int[] yPoints = {arrowY - 12, arrowY + 8, arrowY + 8};
        g.fillPolygon(xPoints, yPoints, 3);
    }

    private Color[] getThemePalette() {
        switch (themeIndex) {
            case 0: // Floresta
                return new Color[]{new Color(18, 36, 24), new Color(10, 18, 14), new Color(120, 220, 120), new Color(120, 240, 160), new Color(40, 120, 80)};
            case 1: // Gelo
                return new Color[]{new Color(20, 40, 60), new Color(10, 20, 40), new Color(120, 200, 255), new Color(160, 220, 255), new Color(60, 120, 160)};
            case 2: // Deserto
                return new Color[]{new Color(60, 40, 20), new Color(40, 25, 10), new Color(240, 200, 120), new Color(255, 220, 160), new Color(180, 120, 60)};
            case 3: // Vulcão
                return new Color[]{new Color(45, 15, 10), new Color(25, 10, 8), new Color(255, 120, 80), new Color(255, 180, 120), new Color(180, 70, 50)};
            default: // Místico
                return new Color[]{new Color(30, 20, 45), new Color(20, 12, 30), new Color(200, 120, 255), new Color(210, 170, 255), new Color(120, 70, 160)};
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                selectedLevel = Math.max(1, selectedLevel - 1);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                selectedLevel = Math.min(stateManager.getMaxUnlockedLevel(), selectedLevel + 1);
                break;
            case KeyEvent.VK_ENTER:
                startSelected();
                break;
            case KeyEvent.VK_ESCAPE:
                stateManager.setState(GameStateManager.MENU_STATE);
                break;
        }
    }

    @Override
    public void keyReleased(int keyCode) { }

    @Override
    public void mouseMoved(int x, int y) {
        for (int i = 1; i <= LevelFactory.TOTAL_LEVELS; i++) {
            if (nodes[i].contains(x, y) && i <= stateManager.getMaxUnlockedLevel()) {
                selectedLevel = i;
                break;
            }
        }
    }

    @Override
    public void mousePressed(int x, int y) {
        if (backBtn.contains(x, y)) {
            stateManager.setState(GameStateManager.MENU_STATE);
            return;
        }
        for (int i = 1; i <= LevelFactory.TOTAL_LEVELS; i++) {
            if (nodes[i].contains(x, y) && i <= stateManager.getMaxUnlockedLevel()) {
                selectedLevel = i;
                startSelected();
                break;
            }
        }
    }

    private void startSelected() {
        stateManager.setCurrentLevel(selectedLevel);
        stateManager.transitionTo(GameStateManager.PLAY_STATE);
    }
}
