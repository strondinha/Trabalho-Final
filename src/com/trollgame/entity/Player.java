package com.trollgame.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.trollgame.main.GamePanel;
import com.trollgame.inputs.KeyHandler;
import com.trollgame.utils.Constants;

public class Player extends Entity {

    private GamePanel gp;
    private KeyHandler keyH;

    public double speedX, speedY;
    public boolean left, right;
    
    // Physics Constants
    private final double MOVE_SPEED = 5.0;
    private final double JUMP_STRENGTH = -14.0;
    
    private boolean onGround = false;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        initPlayer();
    }
    
    private void initPlayer() {
        this.x = 100;
        this.y = 300;
        this.width = Constants.TILE_SIZE - 10;
        this.height = Constants.TILE_SIZE - 2;
    }

    @Override
    public void update() {
        updateMovement();
        applyGravity();
        checkCollisions();
        checkLevelBounds();
    }

    private void updateMovement() {
        if (keyH.leftPressed) {
            speedX = -MOVE_SPEED;
            left = true; 
            right = false;
        } else if (keyH.rightPressed) {
            speedX = MOVE_SPEED;
            right = true; 
            left = false;
        } else {
            speedX = 0;
        }

        if (keyH.upPressed && onGround) {
            speedY = JUMP_STRENGTH;
            onGround = false;
            gp.playSE(2); // Jump Sound
        }
    }
    
    private void applyGravity() {
        speedY += Constants.GRAVITY;
        if (speedY > Constants.MAX_FALL_SPEED) speedY = Constants.MAX_FALL_SPEED;
    }

    private void checkCollisions() {
        // Horizontal Check
        this.x += speedX;
        checkBlockCollision(true);

        // Vertical Check
        this.y += speedY;
        onGround = false;
        checkBlockCollision(false);
    }
    
    private void checkBlockCollision(boolean horizontal) {
        Rectangle pRect = getBounds();
        
        // Use a standard for-loop to avoid ConcurrentModification if possible, or synchronized block in GP
        for (int i = 0; i < gp.blocks.size(); i++) {
            Block b = gp.blocks.get(i);
            if (!b.active || !b.isSolid) continue;

            Rectangle bRect = b.getBounds();
            if (pRect.intersects(bRect)) {
                
                // Specific Block interactions could be delegated
                if (b.type == BlockType.SPIKE || b.type == BlockType.SPIKE_MOVING) {
                    gp.resetLevel();
                    return; 
                }

                if (horizontal) {
                    resolveHorizontalCollision(b);
                } else {
                    resolveVerticalCollision(b);
                }
            }
        }
    }
    
    private void resolveHorizontalCollision(Block b) {
        if (speedX > 0) { // Right
            this.x = b.getX() - this.width;
        } else if (speedX < 0) { // Left
            this.x = b.getX() + b.getWidth();
        }
        speedX = 0;
    }

    private void resolveVerticalCollision(Block b) {
        if (speedY > 0) { // Falling
            this.y = b.getY() - this.height;
            onGround = true;
            speedY = 0; // Stop falling
            
            // Interaction on landing
            if (b.type == BlockType.BREAKABLE) b.activateBreak();
            if (b.type == BlockType.BOUNCE_PAD) {
                speedY = -20;
                onGround = false; // Bounced off
            }
            
        } else if (speedY < 0) { // Jumping up (Head hit)
            this.y = b.getY() + b.getHeight();
            speedY = 0;
        }
    }

    private void checkLevelBounds() {
        if (y > Constants.SCREEN_HEIGHT + 300) {
            gp.resetLevel();
        }
    }

    @Override
    public void draw(Graphics2D g2, int cameraX, int cameraY) {
        // Fallback draw
        g2.setColor(Color.BLUE);
        g2.fillRect((int)(x - cameraX), (int)(y - cameraY), width, height);
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }
}
