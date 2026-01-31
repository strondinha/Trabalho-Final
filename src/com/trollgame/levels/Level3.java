package com.trollgame.levels;

import com.trollgame.entities.*;
import com.trollgame.events.*;

import java.awt.*;

public class Level3 extends Level {

    public Level3() {
        super(3);
        backgroundColor = new Color(255, 240, 220);
    }

    @Override
    public void init() {
        spawnX = 50;
        spawnY = 500;

        player = new Player(spawnX, spawnY);
        goal = new Goal(700, 80, 50, 60);

        platforms.add(new Platform(30, 540, 120, 20));
        platforms.add(new Platform(180, 480, 80, 20));

        movingPlatforms.add(new MovingPlatform(
            100, 400,
            500, 400,
            100, 20,
            2f
        ));

        platforms.add(new Platform(550, 380, 100, 20));

        movingPlatforms.add(new MovingPlatform(
            620, 350,
            620, 100,
            80, 20,
            1.5f
        ));

        platforms.add(new Platform(680, 150, 100, 20));

        traps.add(new Trap(150, 560, 500, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(20, 200, 20, 150, Trap.TrapType.SPIKE_RIGHT));

        EventTrigger trigger1 = EventTrigger.createXTrigger(280, 100,
            EventFactory.platformSpeedChange(0, 4.0f));
        eventTriggers.add(trigger1);

        EventTrigger trigger2 = new EventTrigger(600, 300, 100, 80,
            EventFactory.platformSpeedChange(1, 5.0f));
        eventTriggers.add(trigger2);

        EventTrigger trigger3 = EventTrigger.createXTrigger(250, 50,
            EventFactory.platformDisappear(1));
        eventTriggers.add(trigger3);
    }
}
