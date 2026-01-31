package com.trollgame.levels;

import com.trollgame.entities.*;
import com.trollgame.events.*;

import java.awt.*;

public class Level1 extends Level {

    private Trap hiddenSpikes;

    public Level1() {
        super(1);
        backgroundColor = new Color(255, 250, 240);
    }

    @Override
    public void init() {
        spawnX = 50;
        spawnY = 500;

        player = new Player(spawnX, spawnY);
        goal = new Goal(720, 500, 50, 60);

        platforms.add(new Platform(30, 540, 150, 20));
        platforms.add(new Platform(220, 480, 100, 20));
        platforms.add(new Platform(360, 420, 100, 20));
        platforms.add(new Platform(500, 450, 100, 20));
        platforms.add(new Platform(640, 540, 130, 20));

        hiddenSpikes = new Trap(360, 520, 100, 30, Trap.TrapType.SPIKE_UP);
        hiddenSpikes.hide();
        traps.add(hiddenSpikes);

        EventTrigger trigger1 = EventTrigger.createXTrigger(340, 50,
            EventFactory.platformDisappear(2));
        eventTriggers.add(trigger1);

        EventTrigger trigger2 = EventTrigger.createXTrigger(380, 30,
            EventFactory.spikeEmerge(0));
        eventTriggers.add(trigger2);

        EventTrigger trigger3 = EventTrigger.createXTrigger(600, 40,
            EventFactory.platformDisappear(4));
        eventTriggers.add(trigger3);
    }

    @Override
    protected void resetTrap(Trap t) {
        if (t == hiddenSpikes) {
            hiddenSpikes.hide();
        }
    }

    @Override
    protected boolean isOriginalTrap(Trap t) {
        return t == hiddenSpikes;
    }
}
