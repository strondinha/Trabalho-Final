package com.trollgame.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Base class for all game entities (Player, Blocks, etc).
 */
public abstract class Entity {
    
    public double x, y;
    public int width, height;
    
    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
    
    public abstract void update();
    
    // Draw relative to camera position
    public abstract void draw(Graphics2D g2, int cameraX, int cameraY);
}
