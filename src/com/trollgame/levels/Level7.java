package com.trollgame.levels;

import com.trollgame.entities.*;
import com.trollgame.events.*;

import java.awt.*;

public class Level7 extends Level {

    private Trap hiddenCeilingSpikes;

    public Level7() {
        super(7);
        backgroundColor = new Color(220, 240, 255);
    }

    @Override
    public void init() {
        spawnX = 60;
        spawnY = 480;

        player = new Player(spawnX, spawnY);
        goal = new Goal(700, 60, 50, 60);

        platforms.add(new Platform(30, 520, 160, 20));
        platforms.add(new Platform(230, 460, 100, 20));
        platforms.add(new Platform(380, 400, 100, 20));
        platforms.add(new Platform(520, 340, 100, 20));
        platforms.add(new Platform(660, 280, 100, 20));

        movingPlatforms.add(new MovingPlatform(200, 360, 520, 360, 90, 20, 2.2f));
        movingPlatforms.add(new MovingPlatform(600, 300, 600, 120, 70, 20, 1.8f));

        traps.add(new Trap(150, 560, 500, 20, Trap.TrapType.SPIKE_UP));
        hiddenCeilingSpikes = new Trap(520, 20, 100, 30, Trap.TrapType.SPIKE_DOWN);
        hiddenCeilingSpikes.hide();
        traps.add(hiddenCeilingSpikes);

        eventTriggers.add(EventTrigger.createXTrigger(260, 80, EventFactory.platformSpeedChange(0, 2.2f)));
        eventTriggers.add(new EventTrigger(580, 240, 80, 80, EventFactory.platformSpeedChange(1, 2.6f)));
        eventTriggers.add(new EventTrigger(420, 320, 120, 80, EventFactory.gravityInvert()));
        eventTriggers.add(new EventTrigger(520, 260, 100, 80, EventFactory.spikeEmerge(1)));
    }

    @Override
    protected void resetTrap(Trap t) {
        if (t == hiddenCeilingSpikes) hiddenCeilingSpikes.hide();
    }

    @Override
    protected boolean isOriginalTrap(Trap t) {
        return t == hiddenCeilingSpikes || traps.indexOf(t) == 0;
    }
}
