package com.trollgame.levels;

import com.trollgame.entities.*;
import com.trollgame.events.*;

import java.awt.*;

public class Level10 extends Level {

    private CeilingCollapseEvent ceilingEvent;

    public Level10() {
        super(10);
        backgroundColor = new Color(255, 240, 220);
    }

    @Override
    public void init() {
        spawnX = 70;
        spawnY = 480;

        player = new Player(spawnX, spawnY);
        goal = new Goal(720, 480, 50, 60);

        platforms.add(new Platform(40, 520, 150, 20));
        platforms.add(new Platform(220, 460, 120, 20));
        platforms.add(new Platform(380, 400, 120, 20));
        platforms.add(new Platform(540, 340, 120, 20));
        platforms.add(new Platform(680, 520, 100, 20));

        traps.add(new Trap(260, 560, 120, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(420, 560, 120, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(600, 320, 20, 60, Trap.TrapType.SPIKE_LEFT));

        ceilingEvent = (CeilingCollapseEvent) EventFactory.ceilingCollapse(
            520, 20,
            320,
            140, 30,
            2.6f
        );

        eventTriggers.add(EventTrigger.createXTrigger(520, 80, ceilingEvent));
        eventTriggers.add(EventTrigger.createXTrigger(520, 40, EventFactory.platformDisappear(2)));
        eventTriggers.add(new EventTrigger(220, 420, 120, 60, EventFactory.playerGrow(20)));
    }
}
