package com.trollgame.levels;

import com.trollgame.entities.*;
import com.trollgame.events.*;

import java.awt.*;

public class Level5 extends Level {

    private Trap hiddenCeilingSpikes;
    private Trap hiddenFloorSpikes;

    public Level5() {
        super(5);
        backgroundColor = new Color(255, 230, 200);
    }

    @Override
    public void init() {
        spawnX = 50;
        spawnY = 500;

        player = new Player(spawnX, spawnY);
        goal = new Goal(700, 40, 60, 60);

        platforms.add(new Platform(30, 540, 120, 20));
        platforms.add(new Platform(180, 480, 100, 20));
        platforms.add(new Platform(320, 420, 120, 20));
        platforms.add(new Platform(450, 350, 100, 20));

        platforms.add(new Platform(320, 100, 100, 20));
        platforms.add(new Platform(180, 60, 100, 20));
        platforms.add(new Platform(450, 40, 80, 20));
        platforms.add(new Platform(580, 80, 100, 20));

        traps.add(new Trap(280, 520, 40, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(560, 330, 40, 20, Trap.TrapType.SPIKE_UP));

        hiddenCeilingSpikes = new Trap(400, 20, 60, 25, Trap.TrapType.SPIKE_DOWN);
        hiddenCeilingSpikes.hide();
        traps.add(hiddenCeilingSpikes);

        traps.add(new Trap(650, 20, 30, 25, Trap.TrapType.SPIKE_DOWN));
        traps.add(new Trap(760, 150, 20, 100, Trap.TrapType.SPIKE_LEFT));

        EventTrigger trigger1 = new EventTrigger(450, 300, 100, 60,
            EventFactory.gravityInvert());
        eventTriggers.add(trigger1);

        EventTrigger trigger2 = EventTrigger.createXTrigger(380, 60,
            EventFactory.spikeEmerge(2));
        eventTriggers.add(trigger2);

        EventTrigger trigger3 = EventTrigger.createXTrigger(440, 50,
            EventFactory.platformDisappear(6));
        eventTriggers.add(trigger3);

        EventTrigger trigger4 = EventTrigger.createXTrigger(280, 60,
            EventFactory.platformDisappear(4));
        eventTriggers.add(trigger4);
    }

    @Override
    protected void resetTrap(Trap t) {
        if (t == hiddenCeilingSpikes) {
            hiddenCeilingSpikes.hide();
        }
    }

    @Override
    protected boolean isOriginalTrap(Trap t) {
        return t == hiddenCeilingSpikes ||
               traps.indexOf(t) == 0 ||
               traps.indexOf(t) == 1 ||
               traps.indexOf(t) == 3 ||
               traps.indexOf(t) == 4;
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);

        g.setColor(new Color(200, 100, 50, 150));
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("???", 480, 370);
    }
}
