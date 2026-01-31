package com.trollgame.levels;

import com.trollgame.entities.*;
import com.trollgame.events.*;

import java.awt.*;

public class Level12 extends Level {

    private Trap hiddenCeiling;

    public Level12() {
        super(12);
        backgroundColor = new Color(235, 215, 255);
    }

    @Override
    public void init() {
        spawnX = 60;
        spawnY = 480;

        player = new Player(spawnX, spawnY);
        goal = new Goal(700, 480, 50, 60);

        platforms.add(new Platform(40, 520, 150, 20));
        platforms.add(new Platform(220, 470, 120, 20));
        platforms.add(new Platform(380, 420, 120, 20));
        platforms.add(new Platform(540, 370, 120, 20));
        platforms.add(new Platform(680, 520, 100, 20));

        traps.add(new Trap(260, 560, 120, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(420, 560, 120, 20, Trap.TrapType.SPIKE_UP));

        hiddenCeiling = new Trap(520, 20, 120, 30, Trap.TrapType.SPIKE_DOWN);
        hiddenCeiling.hide();
        traps.add(hiddenCeiling);

        eventTriggers.add(new EventTrigger(520, 320, 120, 80, EventFactory.spikeEmerge(2)));
        eventTriggers.add(EventTrigger.createXTrigger(300, 80, EventFactory.multiplePlatformsDisappear(new int[]{1, 2, 3}, 45)));
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
