package com.trollgame.events;

import com.trollgame.entities.*;
import com.trollgame.levels.Level;

/**
 * Event - Abstract base class for deceptive events
 */
public abstract class Event {
    protected boolean executed;
    protected boolean continuous;

    public Event() {
        this.executed = false;
        this.continuous = false;
    }

    public abstract void execute(Player player, Level level);

    public void update(Level level) {
        // Override for continuous events
    }

    public void reset() {
        executed = false;
    }

    public boolean isExecuted() { return executed; }
    public boolean isContinuous() { return continuous; }
}

class PlatformDisappearEvent extends Event {
    private int platformIndex;

    public PlatformDisappearEvent(int platformIndex) {
        this.platformIndex = platformIndex;
    }

    @Override
    public void execute(Player player, Level level) {
        if (executed) return;
        executed = true;

        if (platformIndex < level.getPlatforms().size()) {
            level.getPlatforms().get(platformIndex).disappear();
        }
    }
}

class FloorOpenEvent extends Event {
    private int platformIndex;
    private Trap hole;

    public FloorOpenEvent(int platformIndex, float holeX, float holeY, int width, int height) {
        this.platformIndex = platformIndex;
        this.hole = new Trap(holeX, holeY, width, height, Trap.TrapType.HOLE);
        this.hole.hide();
    }

    @Override
    public void execute(Player player, Level level) {
        if (executed) return;
        executed = true;

        if (platformIndex >= 0 && platformIndex < level.getPlatforms().size()) {
            level.getPlatforms().get(platformIndex).disappear();
        }
        hole.emerge();
        level.getTraps().add(hole);
    }

    @Override
    public void reset() {
        super.reset();
        hole.hide();
    }
}

class SpikeEmergeEvent extends Event {
    private int trapIndex;

    public SpikeEmergeEvent(int trapIndex) {
        this.trapIndex = trapIndex;
    }

    @Override
    public void execute(Player player, Level level) {
        if (executed) return;
        executed = true;

        if (trapIndex < level.getTraps().size()) {
            level.getTraps().get(trapIndex).emerge();
        }
    }
}

class PlatformSpeedEvent extends Event {
    private int platformIndex;
    private float speedMultiplier;

    public PlatformSpeedEvent(int platformIndex, float speedMultiplier) {
        this.platformIndex = platformIndex;
        this.speedMultiplier = speedMultiplier;
    }

    @Override
    public void execute(Player player, Level level) {
        if (executed) return;
        executed = true;

        if (platformIndex < level.getMovingPlatforms().size()) {
            level.getMovingPlatforms().get(platformIndex).setSpeedMultiplier(speedMultiplier);
        }
    }
}

class PlayerGrowEvent extends Event {
    private int growAmount;

    public PlayerGrowEvent(int growAmount) {
        this.growAmount = growAmount;
    }

    @Override
    public void execute(Player player, Level level) {
        if (executed) return;
        executed = true;

        player.grow(growAmount);
    }
}

class GravityInvertEvent extends Event {
    public GravityInvertEvent() {}

    @Override
    public void execute(Player player, Level level) {
        if (executed) return;
        executed = true;

        player.invertGravity();
    }
}

class MultiplePlatformDisappearEvent extends Event {
    private int[] platformIndices;
    private int currentIndex;
    private int delayFrames;
    private int frameCounter;

    public MultiplePlatformDisappearEvent(int[] platformIndices, int delayFrames) {
        this.platformIndices = platformIndices;
        this.delayFrames = delayFrames;
        this.currentIndex = 0;
        this.frameCounter = 0;
        this.continuous = true;
    }

    @Override
    public void execute(Player player, Level level) {
        if (executed) return;
        executed = true;

        if (platformIndices.length > 0 && platformIndices[0] < level.getPlatforms().size()) {
            level.getPlatforms().get(platformIndices[0]).disappear();
            currentIndex = 1;
        }
    }

    @Override
    public void update(Level level) {
        if (!executed || currentIndex >= platformIndices.length) return;

        frameCounter++;
        if (frameCounter >= delayFrames) {
            frameCounter = 0;
            if (platformIndices[currentIndex] < level.getPlatforms().size()) {
                level.getPlatforms().get(platformIndices[currentIndex]).disappear();
            }
            currentIndex++;
        }
    }

    @Override
    public void reset() {
        super.reset();
        currentIndex = 0;
        frameCounter = 0;
    }
}
