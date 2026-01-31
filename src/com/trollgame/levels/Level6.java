package com.trollgame.levels;

import com.trollgame.entities.*;
import com.trollgame.events.*;

import java.awt.*;

public class Level6 extends Level {

    private Trap hiddenSpikes;

    public Level6() {
        super(6);
        backgroundColor = new Color(230, 250, 235);
    }

    @Override
    public void init() {
        spawnX = 60;
        spawnY = 500;

        player = new Player(spawnX, spawnY);
        goal = new Goal(700, 320, 50, 60);

        platforms.add(new Platform(40, 540, 140, 20));
        platforms.add(new Platform(220, 500, 90, 20));
        platforms.add(new Platform(360, 460, 90, 20));
        platforms.add(new Platform(500, 420, 90, 20));
        platforms.add(new Platform(640, 380, 90, 20));

        traps.add(new Trap(260, 560, 120, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(460, 560, 120, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(620, 340, 30, 40, Trap.TrapType.SPIKE_RIGHT));

        hiddenSpikes = new Trap(500, 400, 90, 20, Trap.TrapType.SPIKE_UP);
        hiddenSpikes.hide();
        traps.add(hiddenSpikes);

        eventTriggers.add(EventTrigger.createXTrigger(470, 40, EventFactory.spikeEmerge(2)));
        eventTriggers.add(EventTrigger.createXTrigger(420, 60, EventFactory.multiplePlatformsDisappear(new int[]{2, 3}, 45)));
    }

    @Override
    protected void resetTrap(Trap t) {
        if (t == hiddenSpikes) hiddenSpikes.hide();
    }

    @Override
    protected boolean isOriginalTrap(Trap t) {
        return t != hiddenSpikes || traps.indexOf(t) < 2;
    }
}
