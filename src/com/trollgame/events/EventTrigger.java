package com.trollgame.events;

import com.trollgame.entities.Player;
import com.trollgame.levels.Level;

/**
 * EventTrigger - Defines when and how a deceptive event should trigger
 */
public class EventTrigger {
    private float triggerX;
    private float triggerY;
    private float triggerWidth;
    private float triggerHeight;
    private boolean triggered;
    private boolean useXOnly;
    private Event event;

    public EventTrigger(float triggerX, float triggerY, float triggerWidth,
                        float triggerHeight, Event event) {
        this.triggerX = triggerX;
        this.triggerY = triggerY;
        this.triggerWidth = triggerWidth;
        this.triggerHeight = triggerHeight;
        this.event = event;
        this.triggered = false;
        this.useXOnly = false;
    }

    public static EventTrigger createXTrigger(float x, float width, Event event) {
        EventTrigger trigger = new EventTrigger(x, 0, width, 600, event);
        trigger.useXOnly = true;
        return trigger;
    }

    public void check(Player player, Level level) {
        if (triggered) return;

        float px = player.getX();
        float py = player.getY();
        int pw = player.getWidth();
        int ph = player.getHeight();

        boolean inTriggerZone;

        if (useXOnly) {
            inTriggerZone = px + pw > triggerX && px < triggerX + triggerWidth;
        } else {
            inTriggerZone = px + pw > triggerX && px < triggerX + triggerWidth &&
                           py + ph > triggerY && py < triggerY + triggerHeight;
        }

        if (inTriggerZone) {
            triggered = true;
            event.execute(player, level);
        }
    }

    public void reset() {
        triggered = false;
        event.reset();
    }

    public void scaleFromOriginX(float origin, float factor) {
        triggerX = origin + (triggerX - origin) * factor;
    }

    public boolean isTriggered() { return triggered; }
    public Event getEvent() { return event; }
}
