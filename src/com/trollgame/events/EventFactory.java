package com.trollgame.events;

/**
 * EventFactory - Creates predefined deceptive events
 */
public class EventFactory {
    public static Event platformDisappear(int platformIndex) {
        return new PlatformDisappearEvent(platformIndex);
    }

    public static Event floorOpen(int platformIndex, float holeX, float holeY, int width, int height) {
        return new FloorOpenEvent(platformIndex, holeX, holeY, width, height);
    }

    public static Event spikeEmerge(int trapIndex) {
        return new SpikeEmergeEvent(trapIndex);
    }

    public static Event platformSpeedChange(int platformIndex, float multiplier) {
        return new PlatformSpeedEvent(platformIndex, multiplier);
    }

    public static Event playerGrow(int amount) {
        return new PlayerGrowEvent(amount);
    }

    public static Event gravityInvert() {
        return new GravityInvertEvent();
    }

    public static Event ceilingCollapse(float x, float startY, float targetY,
                                        int width, int height, float speed) {
        return new CeilingCollapseEvent(x, startY, targetY, width, height, speed);
    }

    public static Event multiplePlatformsDisappear(int[] indices, int delayFrames) {
        return new MultiplePlatformDisappearEvent(indices, delayFrames);
    }
}
