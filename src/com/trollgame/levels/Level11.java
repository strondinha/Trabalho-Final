package com.trollgame.levels;

import com.trollgame.entities.*;
import com.trollgame.events.*;

import java.awt.*;

public class Level11 extends Level {

    private Trap hiddenCeiling;

    public Level11() {
        super(11);
        backgroundColor = new Color(245, 225, 255);
    }

    @Override
    public void init() {
        spawnX = 60;
        spawnY = 500;

        player = new Player(spawnX, spawnY);
        goal = new Goal(720, 80, 50, 60);

        platforms.add(new Platform(40, 540, 140, 20));
        platforms.add(new Platform(200, 500, 100, 20));
        platforms.add(new Platform(340, 460, 100, 20));
        platforms.add(new Platform(480, 420, 100, 20));
        platforms.add(new Platform(620, 380, 100, 20));
        platforms.add(new Platform(680, 140, 100, 20));

        movingPlatforms.add(new MovingPlatform(260, 360, 520, 360, 90, 20, 2.0f));
        movingPlatforms.add(new MovingPlatform(620, 300, 620, 140, 70, 20, 1.6f));

        traps.add(new Trap(180, 560, 160, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(420, 560, 160, 20, Trap.TrapType.SPIKE_UP));

        hiddenCeiling = new Trap(520, 20, 120, 30, Trap.TrapType.SPIKE_DOWN);
        hiddenCeiling.hide();
        traps.add(hiddenCeiling);

        eventTriggers.add(EventTrigger.createXTrigger(280, 60, EventFactory.platformSpeedChange(0, 2.2f)));
        eventTriggers.add(new EventTrigger(600, 240, 80, 80, EventFactory.platformSpeedChange(1, 2.6f)));
        eventTriggers.add(new EventTrigger(520, 260, 120, 80, EventFactory.spikeEmerge(2)));
        eventTriggers.add(EventTrigger.createXTrigger(520, 40, EventFactory.platformDisappear(2)));
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
