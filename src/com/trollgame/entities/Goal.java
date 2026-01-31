package com.trollgame.entities;

import java.awt.*;

/**
 * Goal - The exit/goal area for each level
 */
public class Goal {
    private float x, y;
    private int width, height;
    private Color goalColor = new Color(255, 230, 100);
    private Color borderColor = new Color(220, 180, 50);
    private float pulseTimer = 0;

    public Goal(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update() {
        pulseTimer += 0.1f;
    }

    public void render(Graphics2D g) {
        float pulse = (float)(Math.sin(pulseTimer) * 0.2 + 0.8);
        int alpha = (int)(200 * pulse);
        g.setColor(new Color(255, 200, 80, 80));
        g.fillRect((int)x - 6, (int)y - 6, width + 12, height + 12);

        g.setColor(new Color(0, 0, 0, 60));
        g.fillRect((int)x + 3, (int)y + 4, width, height);

        g.setColor(new Color(255, 230, 100, alpha));
        g.fillRect((int)x, (int)y, width, height);

        g.setColor(borderColor);
        g.setStroke(new BasicStroke(3));
        g.drawRect((int)x, (int)y, width, height);

        g.setColor(new Color(150, 100, 30));
        g.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g.getFontMetrics();
        String text = "EXIT";
        int textX = (int)x + (width - fm.stringWidth(text)) / 2;
        int textY = (int)y + (height + fm.getAscent()) / 2 - 2;
        g.drawString(text, textX, textY);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public void setX(float x) { this.x = x; }
}
