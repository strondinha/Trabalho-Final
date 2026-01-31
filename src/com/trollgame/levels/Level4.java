package com.trollgame.levels;

import com.trollgame.entities.*;
import com.trollgame.events.*;

import java.awt.*;

public class Level4 extends Level {

    private CeilingCollapseEvent ceilingEvent;
    private Trap originalCeiling;

    public Level4() {
        super(4);
        backgroundColor = new Color(255, 235, 215);
    }

    @Override
    public void init() {
        spawnX = 50;
        spawnY = 480;

        player = new Player(spawnX, spawnY);
        goal = new Goal(720, 480, 50, 60);

        platforms.add(new Platform(30, 520, 150, 20));
        platforms.add(new Platform(200, 450, 150, 20));
        platforms.add(new Platform(200, 380, 150, 20));
        platforms.add(new Platform(380, 480, 120, 20));
        platforms.add(new Platform(520, 520, 130, 20));
        platforms.add(new Platform(680, 540, 100, 20));

        traps.add(new Trap(350, 500, 30, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(650, 520, 30, 20, Trap.TrapType.SPIKE_UP));

        ceilingEvent = (CeilingCollapseEvent) EventFactory.ceilingCollapse(
            520, 20,
            400,
            130, 30,
            3f
        );
        originalCeiling = ceilingEvent.getCeiling();

        EventTrigger trigger1 = new EventTrigger(210, 400, 80, 60,
            EventFactory.playerGrow(25));
        eventTriggers.add(trigger1);

        EventTrigger trigger2 = EventTrigger.createXTrigger(520, 80, ceilingEvent);
        eventTriggers.add(trigger2);

        EventTrigger trigger3 = EventTrigger.createXTrigger(480, 40,
            EventFactory.platformDisappear(3));
        eventTriggers.add(trigger3);
    }

    @Override
    protected boolean isOriginalTrap(Trap t) {
        return t == originalCeiling || traps.indexOf(t) <= 1;
    }

    @Override
    protected void resetLevelState() {
        if (originalCeiling != null) {
            originalCeiling.setY(20);
            originalCeiling.hide();
        }
    }
}
