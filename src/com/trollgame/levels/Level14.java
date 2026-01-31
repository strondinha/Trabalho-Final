package com.trollgame.levels;

import com.trollgame.entities.*;
import com.trollgame.events.*;

import java.awt.*;

public class Level14 extends Level {

    private Trap hiddenCeiling;
    private CeilingCollapseEvent ceilingEvent;

    public Level14() {
        super(14);
        backgroundColor = new Color(255, 220, 200);
    }

    @Override
    public void init() {
        spawnX = 70;
        spawnY = 480;

        player = new Player(spawnX, spawnY);
        goal = new Goal(700, 480, 50, 60);

        platforms.add(new Platform(40, 520, 160, 20));
        platforms.add(new Platform(240, 470, 120, 20));
        platforms.add(new Platform(400, 420, 120, 20));
        platforms.add(new Platform(560, 370, 120, 20));
        platforms.add(new Platform(680, 520, 100, 20));

        traps.add(new Trap(280, 560, 140, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(460, 560, 140, 20, Trap.TrapType.SPIKE_UP));

        hiddenCeiling = new Trap(520, 20, 120, 30, Trap.TrapType.SPIKE_DOWN);
        hiddenCeiling.hide();
        traps.add(hiddenCeiling);

        ceilingEvent = (CeilingCollapseEvent) EventFactory.ceilingCollapse(
            520, 20,
            360,
            120, 30,
            2.6f
        );

        eventTriggers.add(new EventTrigger(520, 320, 120, 80, EventFactory.spikeEmerge(2)));
        eventTriggers.add(EventTrigger.createXTrigger(520, 40, EventFactory.platformDisappear(2)));
        eventTriggers.add(new EventTrigger(240, 420, 100, 60, EventFactory.playerGrow(22)));
        eventTriggers.add(EventTrigger.createXTrigger(520, 80, ceilingEvent));
    }

    @Override
    protected void resetTrap(Trap t) {
        if (t == hiddenCeiling) hiddenCeiling.hide();
    }

    @Override
    protected boolean isOriginalTrap(Trap t) {
        return t == hiddenCeiling || traps.indexOf(t) < 2;
    }
}
