package com.trollgame.levels;

import com.trollgame.entities.*;
import com.trollgame.events.*;

import java.awt.*;

public class Level8 extends Level {

    private Trap hiddenCeiling;

    public Level8() {
        super(8);
        backgroundColor = new Color(210, 235, 255);
    }

    @Override
    public void init() {
        spawnX = 70;
        spawnY = 500;

        player = new Player(spawnX, spawnY);
        goal = new Goal(720, 500, 50, 60);

        platforms.add(new Platform(40, 540, 140, 20));
        platforms.add(new Platform(220, 500, 120, 20));
        platforms.add(new Platform(380, 460, 120, 20));
        platforms.add(new Platform(540, 420, 120, 20));
        platforms.add(new Platform(680, 540, 100, 20));

        traps.add(new Trap(300, 560, 120, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(480, 560, 120, 20, Trap.TrapType.SPIKE_UP));

        hiddenCeiling = new Trap(520, 20, 120, 30, Trap.TrapType.SPIKE_DOWN);
        hiddenCeiling.hide();
        traps.add(hiddenCeiling);

        eventTriggers.add(new EventTrigger(520, 380, 120, 80, EventFactory.spikeEmerge(2)));
        eventTriggers.add(EventTrigger.createXTrigger(720, 60, EventFactory.platformDisappear(4)));
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
