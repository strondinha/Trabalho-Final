package com.trollgame.entities;

import java.awt.*;

/**
 * Platform - Static platform that player can stand on
 */
public class Platform {
    protected float x, y;
    protected int width, height;
    protected boolean visible;
    protected boolean solid;

    protected Color platformColor = new Color(255, 220, 180);
    protected Color borderColor = new Color(200, 150, 100);

    public Platform(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = true;
        this.solid = true;
    }

    public void update() {
        // Static platforms don't update by default
    }

    public void render(Graphics2D g) {
        if (!visible) return;

        // Sombra suave
        g.setColor(new Color(0, 0, 0, 60));
        g.fillRect((int)x + 3, (int)y + 4, width, height);

        g.setColor(platformColor);
        g.fillRect((int)x, (int)y, width, height);

        g.setColor(borderColor);
        g.setStroke(new BasicStroke(2));
        g.drawRect((int)x, (int)y, width, height);
    }

    public void disappear() {
        visible = false;
        solid = false;
    }

    public void appear() {
        visible = true;
        solid = true;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isVisible() { return visible; }
    public boolean isSolid() { return solid; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public void setSolid(boolean solid) { this.solid = solid; }
}
