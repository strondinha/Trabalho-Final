package com.trollgame.levels;

import com.trollgame.entities.*;
import com.trollgame.events.*;
import java.awt.*;

public class Level15 extends Level {

    public Level15() {
        super(15);
        backgroundColor = new Color(240, 210, 200);
    }

    @Override
    public void init() {
        spawnX = 60;
        spawnY = 480;

        player = new Player(spawnX, spawnY);
        goal = new Goal(1680, 140, 50, 60);

        platforms.add(new Platform(40, 520, 150, 20));
        platforms.add(new Platform(220, 470, 120, 20));
        platforms.add(new Platform(380, 420, 120, 20));
        platforms.add(new Platform(540, 370, 120, 20));
        platforms.add(new Platform(700, 320, 120, 20));
        platforms.add(new Platform(860, 280, 120, 20));
        platforms.add(new Platform(1020, 340, 120, 20));
        platforms.add(new Platform(1180, 300, 120, 20));
        platforms.add(new Platform(1340, 260, 120, 20));
        platforms.add(new Platform(1500, 220, 120, 20));
        platforms.add(new Platform(1620, 200, 120, 20));

        movingPlatforms.add(new MovingPlatform(200, 300, 520, 300, 90, 20, 2.2f));
        movingPlatforms.add(new MovingPlatform(640, 260, 640, 120, 70, 20, 1.9f));
        movingPlatforms.add(new MovingPlatform(980, 260, 1280, 260, 90, 20, 2.0f));

        traps.add(new Trap(180, 560, 220, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(470, 560, 220, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(760, 560, 220, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(1120, 560, 220, 20, Trap.TrapType.SPIKE_UP));

        eventTriggers.add(EventTrigger.createXTrigger(260, 80, EventFactory.platformSpeedChange(0, 2.2f)));
        eventTriggers.add(new EventTrigger(600, 220, 80, 80, EventFactory.platformSpeedChange(1, 2.6f)));
        eventTriggers.add(new EventTrigger(980, 240, 120, 80, EventFactory.platformSpeedChange(2, 2.4f)));
    }
}
