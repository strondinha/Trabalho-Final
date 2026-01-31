package com.trollgame.entities;

import java.awt.*;

/**
 * Trap - Deadly obstacle that kills the player on contact
 */
public class Trap {
    protected float x, y;
    protected int width, height;
    protected boolean active;
    protected boolean visible;
    protected TrapType type;

    protected Color trapColor = new Color(220, 80, 60);
    protected Color darkColor = new Color(150, 40, 30);

    public enum TrapType {
        SPIKE_UP,
        SPIKE_DOWN,
        SPIKE_LEFT,
        SPIKE_RIGHT,
        HOLE,
        CRUSHING_CEILING
    }

    public Trap(float x, float y, int width, int height, TrapType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.active = true;
        this.visible = true;
    }

    public void update() {
        // Override for animated traps
    }

    public void render(Graphics2D g) {
        if (!visible) return;

        switch (type) {
            case SPIKE_UP: drawSpikesUp(g); break;
            case SPIKE_DOWN: drawSpikesDown(g); break;
            case SPIKE_LEFT: drawSpikesLeft(g); break;
            case SPIKE_RIGHT: drawSpikesRight(g); break;
            case HOLE: drawHole(g); break;
            case CRUSHING_CEILING: drawCrushingCeiling(g); break;
        }
    }

    private void drawSpikesUp(Graphics2D g) {
        int spikeCount = width / 15;
        int spikeWidth = width / spikeCount;

        for (int i = 0; i < spikeCount; i++) {
            int[] xPoints = {
                (int)x + i * spikeWidth,
                (int)x + i * spikeWidth + spikeWidth / 2,
                (int)x + (i + 1) * spikeWidth
            };
            int[] yPoints = {
                (int)y + height,
                (int)y,
                (int)y + height
            };

            g.setColor(trapColor);
            g.fillPolygon(xPoints, yPoints, 3);
            g.setColor(darkColor);
            g.setStroke(new BasicStroke(1));
            g.drawPolygon(xPoints, yPoints, 3);
        }
    }

    private void drawSpikesDown(Graphics2D g) {
        int spikeCount = width / 15;
        int spikeWidth = width / spikeCount;

        for (int i = 0; i < spikeCount; i++) {
            int[] xPoints = {
                (int)x + i * spikeWidth,
                (int)x + i * spikeWidth + spikeWidth / 2,
                (int)x + (i + 1) * spikeWidth
            };
            int[] yPoints = {
                (int)y,
                (int)y + height,
                (int)y
            };

            g.setColor(trapColor);
            g.fillPolygon(xPoints, yPoints, 3);
            g.setColor(darkColor);
            g.drawPolygon(xPoints, yPoints, 3);
        }
    }

    private void drawSpikesLeft(Graphics2D g) {
        int spikeCount = height / 15;
        int spikeHeight = height / spikeCount;

        for (int i = 0; i < spikeCount; i++) {
            int[] xPoints = {
                (int)x + width,
                (int)x,
                (int)x + width
            };
            int[] yPoints = {
                (int)y + i * spikeHeight,
                (int)y + i * spikeHeight + spikeHeight / 2,
                (int)y + (i + 1) * spikeHeight
            };

            g.setColor(trapColor);
            g.fillPolygon(xPoints, yPoints, 3);
            g.setColor(darkColor);
            g.drawPolygon(xPoints, yPoints, 3);
        }
    }

    private void drawSpikesRight(Graphics2D g) {
        int spikeCount = height / 15;
        int spikeHeight = height / spikeCount;

        for (int i = 0; i < spikeCount; i++) {
            int[] xPoints = {
                (int)x,
                (int)x + width,
                (int)x
            };
            int[] yPoints = {
                (int)y + i * spikeHeight,
                (int)y + i * spikeHeight + spikeHeight / 2,
                (int)y + (i + 1) * spikeHeight
            };

            g.setColor(trapColor);
            g.fillPolygon(xPoints, yPoints, 3);
            g.setColor(darkColor);
            g.drawPolygon(xPoints, yPoints, 3);
        }
    }

    private void drawHole(Graphics2D g) {
        g.setColor(new Color(40, 20, 20));
        g.fillRect((int)x, (int)y, width, height);
    }

    private void drawCrushingCeiling(Graphics2D g) {
        g.setColor(new Color(180, 100, 80));
        g.fillRect((int)x, (int)y, width, height);
        g.setColor(darkColor);
        g.setStroke(new BasicStroke(3));
        g.drawRect((int)x, (int)y, width, height);

        g.setColor(trapColor);
        for (int i = 0; i < width; i += 20) {
            g.fillRect((int)x + i, (int)y + height - 5, 10, 5);
        }
    }

    public void emerge() {
        visible = true;
        active = true;
    }

    public void hide() {
        visible = false;
        active = false;
    }

    public Rectangle getBounds() {
        if (!active) return new Rectangle(0, 0, 0, 0);

        switch (type) {
            case SPIKE_UP:
                return new Rectangle((int)x + 3, (int)y, width - 6, height - 5);
            case SPIKE_DOWN:
                return new Rectangle((int)x + 3, (int)y + 5, width - 6, height - 5);
            default:
                return new Rectangle((int)x, (int)y, width, height);
        }
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public TrapType getType() { return type; }
}
