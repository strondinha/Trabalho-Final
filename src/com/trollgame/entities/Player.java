package com.trollgame.entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Player - The main player entity
 */
public class Player {
    private float x, y;
    private float velocityX, velocityY;
    private int width, height;
    private int originalWidth, originalHeight;

    private static final float MOVE_SPEED = 5.5f;
    private static final float JUMP_FORCE = -15f;
    private static final float JUMP_CUT_MULTIPLIER = 0.4f;
    private static final float GRAVITY = 0.6f;
    private static final float MAX_FALL_SPEED = 17f;
    private static final float ACCELERATION = 0.8f;
    private static final float FRICTION = 0.85f;

    private static final int COYOTE_TIME = 6;
    private static final int JUMP_BUFFER_TIME = 8;
    private int coyoteCounter;
    private int jumpBufferCounter;
    private boolean jumpHeld;
    private boolean canCutJump;

    private boolean onGround;
    private boolean wasOnGround;
    private boolean movingLeft;
    private boolean movingRight;
    private boolean alive;
    private boolean gravityInverted;

    private Color playerColor = new Color(255, 150, 100);
    private Color outlineColor = new Color(200, 80, 40);
    private float squashStretch = 1.0f;
    private float targetSquash = 1.0f;
    private List<Particle> particles;
    private float landingImpact = 0;

    private BufferedImage sprite;

    private class Particle {
        float x, y, vx, vy, life, maxLife;
        Color color;

        Particle(float x, float y, float vx, float vy, float life, Color color) {
            this.x = x; this.y = y;
            this.vx = vx; this.vy = vy;
            this.life = life; this.maxLife = life;
            this.color = color;
        }

        void update() {
            x += vx;
            y += vy;
            vy += 0.2f;
            life--;
        }

        boolean isDead() { return life <= 0; }
    }

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        this.width = 30;
        this.height = 30;
        this.originalWidth = width;
        this.originalHeight = height;
        this.velocityX = 0;
        this.velocityY = 0;
        this.onGround = false;
        this.wasOnGround = false;
        this.movingLeft = false;
        this.movingRight = false;
        this.alive = true;
        this.gravityInverted = false;
        this.coyoteCounter = 0;
        this.jumpBufferCounter = 0;
        this.jumpHeld = false;
        this.canCutJump = false;
        this.particles = new ArrayList<>();
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public void update() {
        if (!alive) return;

        if (onGround) {
            coyoteCounter = COYOTE_TIME;
        } else if (coyoteCounter > 0) {
            coyoteCounter--;
        }

        if (jumpBufferCounter > 0) {
            jumpBufferCounter--;
            if (canJump()) {
                executeJump();
            }
        }

        if (canCutJump && !jumpHeld && !gravityInverted && velocityY < 0) {
            velocityY *= JUMP_CUT_MULTIPLIER;
            canCutJump = false;
        } else if (canCutJump && !jumpHeld && gravityInverted && velocityY > 0) {
            velocityY *= JUMP_CUT_MULTIPLIER;
            canCutJump = false;
        }

        float targetVelX = 0;
        if (movingLeft) targetVelX = -MOVE_SPEED;
        if (movingRight) targetVelX = MOVE_SPEED;

        if (targetVelX != 0) {
            velocityX += (targetVelX - velocityX) * ACCELERATION;
            if (onGround && Math.random() < 0.3) {
                spawnDustParticle();
            }
        } else {
            velocityX *= FRICTION;
            if (Math.abs(velocityX) < 0.1f) velocityX = 0;
        }

        if (gravityInverted) {
            velocityY -= GRAVITY;
            if (velocityY < -MAX_FALL_SPEED) velocityY = -MAX_FALL_SPEED;
        } else {
            velocityY += GRAVITY;
            if (velocityY > MAX_FALL_SPEED) velocityY = MAX_FALL_SPEED;
        }

        x += velocityX;
        y += velocityY;

        updateSquashStretch();
        updateParticles();

        if (onGround && !wasOnGround) {
            onLand();
        }
        wasOnGround = onGround;
    }

    private void updateSquashStretch() {
        squashStretch += (targetSquash - squashStretch) * 0.3f;

        if (!onGround) {
            if (Math.abs(velocityY) > 5) {
                targetSquash = velocityY < 0 ? 1.2f : 0.8f;
            } else {
                targetSquash = 1.0f;
            }
        } else {
            targetSquash = 1.0f;
        }
    }

    private void onLand() {
        float impact = Math.min(Math.abs(velocityY) / MAX_FALL_SPEED, 1.0f);
        squashStretch = 1.0f - impact * 0.3f;
        landingImpact = impact;

        for (int i = 0; i < (int)(impact * 8) + 2; i++) {
            spawnLandingParticle();
        }
    }

    private void spawnDustParticle() {
        float px = x + width / 2 + (float)(Math.random() - 0.5) * width;
        float py = y + height;
        float pvx = -velocityX * 0.1f + (float)(Math.random() - 0.5) * 2;
        float pvy = -(float)(Math.random() * 2);
        Color dustColor = new Color(200, 180, 150, 150);
        particles.add(new Particle(px, py, pvx, pvy, 15, dustColor));
    }

    private void spawnLandingParticle() {
        float px = x + width / 2 + (float)(Math.random() - 0.5) * width;
        float py = y + height;
        float pvx = (float)(Math.random() - 0.5) * 4;
        float pvy = -(float)(Math.random() * 3 + 1);
        Color dustColor = new Color(200, 180, 150, 200);
        particles.add(new Particle(px, py, pvx, pvy, 20, dustColor));
    }

    private void spawnJumpParticle() {
        for (int i = 0; i < 5; i++) {
            float px = x + width / 2 + (float)(Math.random() - 0.5) * width;
            float py = y + height;
            float pvx = (float)(Math.random() - 0.5) * 3;
            float pvy = (float)(Math.random() * 2 + 1);
            Color jumpColor = new Color(255, 200, 150, 180);
            particles.add(new Particle(px, py, pvx, pvy, 15, jumpColor));
        }
    }

    private void updateParticles() {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.update();
            if (p.isDead()) it.remove();
        }
    }

    public void render(Graphics2D g) {
        if (!alive) return;

        for (Particle p : particles) {
            float alpha = p.life / p.maxLife;
            Color c = new Color(p.color.getRed(), p.color.getGreen(), p.color.getBlue(), (int)(alpha * p.color.getAlpha()));
            g.setColor(c);
            int size = (int)(4 * alpha) + 2;
            g.fillOval((int)p.x - size/2, (int)p.y - size/2, size, size);
        }

        float stretchHeight = height * squashStretch;
        float stretchWidth = width / squashStretch;
        float offsetX = (width - stretchWidth) / 2;
        float offsetY = height - stretchHeight;

        g.setColor(new Color(0, 0, 0, 30));
        g.fillOval((int)(x + offsetX - 2), (int)(y + height - 5), (int)stretchWidth + 4, 10);

        if (sprite != null) {
            g.drawImage(sprite, (int)(x + offsetX), (int)(y + offsetY), (int)stretchWidth, (int)stretchHeight, null);
        } else {
            g.setColor(playerColor);
            g.fillRoundRect((int)(x + offsetX), (int)(y + offsetY), (int)stretchWidth, (int)stretchHeight, 10, 10);

            g.setColor(outlineColor);
            g.setStroke(new BasicStroke(2.5f));
            g.drawRoundRect((int)(x + offsetX), (int)(y + offsetY), (int)stretchWidth, (int)stretchHeight, 10, 10);

            g.setColor(new Color(255, 200, 170, 100));
            g.fillRoundRect((int)(x + offsetX + 3), (int)(y + offsetY + 3), (int)(stretchWidth * 0.4f), (int)(stretchHeight * 0.3f), 5, 5);

            g.setColor(outlineColor);
            int eyeY = gravityInverted ? (int)(y + offsetY + stretchHeight - 14) : (int)(y + offsetY + 8);
            float eyeOffsetX = offsetX + (stretchWidth - width) / 2;

            int eyeOpenness = velocityY < -5 ? 7 : (velocityY > 5 ? 3 : 5);
            g.fillOval((int)(x + eyeOffsetX + 7), eyeY, 6, eyeOpenness);
            g.fillOval((int)(x + eyeOffsetX + width - 13), eyeY, 6, eyeOpenness);

            g.setColor(Color.WHITE);
            g.fillOval((int)(x + eyeOffsetX + 8), eyeY + 1, 2, 2);
            g.fillOval((int)(x + eyeOffsetX + width - 12), eyeY + 1, 2, 2);
        }
    }

    public void jump() {
        jumpHeld = true;
        jumpBufferCounter = JUMP_BUFFER_TIME;

        if (canJump()) {
            executeJump();
        }
    }

    public void releaseJump() {
        jumpHeld = false;
    }

    private boolean canJump() {
        return coyoteCounter > 0 || onGround;
    }

    private void executeJump() {
        if (gravityInverted) {
            velocityY = -JUMP_FORCE;
        } else {
            velocityY = JUMP_FORCE;
        }
        onGround = false;
        coyoteCounter = 0;
        jumpBufferCounter = 0;
        canCutJump = true;

        squashStretch = 0.7f;
        targetSquash = 1.3f;

        spawnJumpParticle();
    }

    public void kill() { alive = false; }

    public void respawn(float spawnX, float spawnY) {
        x = spawnX;
        y = spawnY;
        velocityX = 0;
        velocityY = 0;
        alive = true;
        onGround = false;
        wasOnGround = false;
        gravityInverted = false;
        width = originalWidth;
        height = originalHeight;
        coyoteCounter = 0;
        jumpBufferCounter = 0;
        jumpHeld = false;
        canCutJump = false;
        squashStretch = 1.0f;
        targetSquash = 1.0f;
        particles.clear();
    }

    public void grow(int amount) {
        height += amount;
        if (onGround && !gravityInverted) {
            y -= amount;
        }
    }

    public void invertGravity() {
        gravityInverted = !gravityInverted;
        velocityY = 0;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public float getVelocityX() { return velocityX; }
    public float getVelocityY() { return velocityY; }
    public void setVelocityX(float vx) { this.velocityX = vx; }
    public void setVelocityY(float vy) { this.velocityY = vy; }
    public boolean isOnGround() { return onGround; }
    public void setOnGround(boolean onGround) { this.onGround = onGround; }
    public boolean isAlive() { return alive; }
    public void setMovingLeft(boolean moving) { this.movingLeft = moving; }
    public void setMovingRight(boolean moving) { this.movingRight = moving; }
    public boolean isGravityInverted() { return gravityInverted; }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}
