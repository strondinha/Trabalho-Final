package com.trollgame.events;

import com.trollgame.entities.Player;
import com.trollgame.entities.Trap;
import com.trollgame.levels.Level;

/**
 * CeilingCollapseEvent - Makes the ceiling fall
 */
public class CeilingCollapseEvent extends Event {
    private Trap ceiling;
    private float startY;
    private float targetY;
    private float speed;

    public CeilingCollapseEvent(float x, float startY, float targetY, int width, int height, float speed) {
        this.ceiling = new Trap(x, startY, width, height, Trap.TrapType.CRUSHING_CEILING);
        this.ceiling.hide();
        this.startY = startY;
        this.targetY = targetY;
        this.speed = speed;
        this.continuous = true;
    }

    @Override
    public void execute(Player player, Level level) {
        if (executed) return;
        executed = true;

        ceiling.emerge();
        if (!level.getTraps().contains(ceiling)) {
            level.getTraps().add(ceiling);
        }
    }

    @Override
    public void update(Level level) {
        if (!executed) return;

        if (ceiling.getY() < targetY) {
            ceiling.setY(ceiling.getY() + speed);
        }
    }

    @Override
    public void reset() {
        super.reset();
        ceiling.setY(startY);
        ceiling.hide();
    }

    public Trap getCeiling() {
        return ceiling;
    }
}
