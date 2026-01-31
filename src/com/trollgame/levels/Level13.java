package com.trollgame.levels;

import com.trollgame.entities.*;
import com.trollgame.events.*;

import java.awt.*;

public class Level13 extends Level {

    public Level13() {
        super(13);
        backgroundColor = new Color(255, 230, 210);
    }

    @Override
    public void init() {
        spawnX = 60;
        spawnY = 500;

        player = new Player(spawnX, spawnY);
        goal = new Goal(720, 100, 50, 60);

        platforms.add(new Platform(40, 540, 140, 20));
        platforms.add(new Platform(220, 500, 110, 20));
        platforms.add(new Platform(360, 460, 110, 20));
        platforms.add(new Platform(500, 420, 110, 20));
        platforms.add(new Platform(640, 380, 110, 20));

        movingPlatforms.add(new MovingPlatform(180, 340, 520, 340, 90, 20, 2.0f));
        movingPlatforms.add(new MovingPlatform(620, 300, 620, 140, 70, 20, 1.7f));

        traps.add(new Trap(220, 560, 100, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(380, 560, 100, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(540, 560, 100, 20, Trap.TrapType.SPIKE_UP));

        eventTriggers.add(EventTrigger.createXTrigger(360, 40, EventFactory.platformDisappear(2)));
        eventTriggers.add(EventTrigger.createXTrigger(260, 80, EventFactory.platformSpeedChange(0, 2.2f)));
        eventTriggers.add(new EventTrigger(600, 240, 80, 80, EventFactory.platformSpeedChange(1, 2.6f)));
    }
}
