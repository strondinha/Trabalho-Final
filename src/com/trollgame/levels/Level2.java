package com.trollgame.levels;

import com.trollgame.entities.*;
import com.trollgame.events.*;

import java.awt.*;

public class Level2 extends Level {

    private Trap hiddenCeilingSpikes;
    private Platform floorSection;

    public Level2() {
        super(2);
        backgroundColor = new Color(255, 245, 230);
    }

    @Override
    public void init() {
        spawnX = 50;
        spawnY = 500;

        player = new Player(spawnX, spawnY);
        goal = new Goal(720, 480, 50, 60);

        platforms.add(new Platform(30, 540, 200, 20));

        floorSection = new Platform(230, 540, 100, 20);
        platforms.add(floorSection);

        platforms.add(new Platform(330, 540, 150, 20));
        platforms.add(new Platform(400, 420, 120, 20));
        platforms.add(new Platform(520, 480, 100, 20));
        platforms.add(new Platform(620, 540, 160, 20));

        traps.add(new Trap(100, 520, 60, 20, Trap.TrapType.SPIKE_UP));

        hiddenCeilingSpikes = new Trap(400, 20, 120, 30, Trap.TrapType.SPIKE_DOWN);
        hiddenCeilingSpikes.hide();
        traps.add(hiddenCeilingSpikes);

        EventTrigger trigger1 = EventTrigger.createXTrigger(250, 60,
            EventFactory.floorOpen(1, 230, 540, 100, 60));
        eventTriggers.add(trigger1);

        EventTrigger trigger2 = new EventTrigger(400, 380, 120, 60,
            EventFactory.spikeEmerge(1));
        eventTriggers.add(trigger2);

        EventTrigger trigger3 = EventTrigger.createXTrigger(520, 80,
            EventFactory.platformDisappear(4));
        eventTriggers.add(trigger3);
    }

    @Override
    protected void resetTrap(Trap t) {
        if (t == hiddenCeilingSpikes) {
            hiddenCeilingSpikes.hide();
        }
    }

    @Override
    protected boolean isOriginalTrap(Trap t) {
        return t == hiddenCeilingSpikes || traps.indexOf(t) == 0;
    }
}
