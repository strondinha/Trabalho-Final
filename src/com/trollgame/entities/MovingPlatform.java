package com.trollgame.entities;

import java.awt.*;

/**
 * MovingPlatform - Platform that moves between two points
 */
public class MovingPlatform extends Platform {
    private float startX, startY;
    private float endX, endY;
    private float speed;
    private float originalSpeed;
    private boolean movingToEnd;

    private float lastX, lastY;

    public MovingPlatform(float startX, float startY, float endX, float endY,
                          int width, int height, float speed) {
        super(startX, startY, width, height);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.speed = speed;
        this.originalSpeed = speed;
        this.movingToEnd = true;
        this.lastX = x;
        this.lastY = y;

        this.platformColor = new Color(255, 200, 150);
        this.borderColor = new Color(220, 130, 80);
    }

    @Override
    public void update() {
        if (!visible) return;

        lastX = x;
        lastY = y;

        float targetX = movingToEnd ? endX : startX;
        float targetY = movingToEnd ? endY : startY;

        float dx = targetX - x;
        float dy = targetY - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < speed) {
            x = targetX;
            y = targetY;
            movingToEnd = !movingToEnd;
        } else {
            x += (dx / distance) * speed;
            y += (dy / distance) * speed;
        }
    }

    @Override
    public void render(Graphics2D g) {
        if (!visible) return;

        g.setColor(new Color(0, 0, 0, 60));
        g.fillRect((int)x + 3, (int)y + 4, width, height);

        g.setColor(platformColor);
        g.fillRect((int)x, (int)y, width, height);

        g.setColor(new Color(255, 255, 255, 80));
        g.fillRect((int)x, (int)y, width, height / 3);

        g.setColor(borderColor);
        g.setStroke(new BasicStroke(2));
        g.drawRect((int)x, (int)y, width, height);
    }

    public void setSpeedMultiplier(float multiplier) {
        this.speed = originalSpeed * multiplier;
    }

    public void resetSpeed() {
        this.speed = originalSpeed;
    }

    public void scaleFromOriginX(float origin, float factor) {
        startX = origin + (startX - origin) * factor;
        endX = origin + (endX - origin) * factor;
        x = origin + (x - origin) * factor;
        lastX = x;
    }

    public float getStartX() { return startX; }
    public float getEndX() { return endX; }

    public float getDeltaX() { return x - lastX; }
    public float getDeltaY() { return y - lastY; }
    public float getSpeed() { return speed; }
}
