package com.trollgame.levels;

import com.trollgame.entities.*;
import com.trollgame.events.*;

import java.awt.*;

public class Level9 extends Level {

    public Level9() {
        super(9);
        backgroundColor = new Color(220, 230, 255);
    }

    @Override
    public void init() {
        spawnX = 60;
        spawnY = 500;

        player = new Player(spawnX, spawnY);
        goal = new Goal(700, 240, 50, 60);

        platforms.add(new Platform(40, 540, 160, 20));
        platforms.add(new Platform(240, 480, 120, 20));
        platforms.add(new Platform(400, 420, 120, 20));
        platforms.add(new Platform(560, 360, 120, 20));
        platforms.add(new Platform(680, 300, 100, 20));
        platforms.add(new Platform(620, 260, 140, 20));

        movingPlatforms.add(new MovingPlatform(220, 320, 520, 320, 100, 20, 1.3f));

        traps.add(new Trap(360, 560, 120, 20, Trap.TrapType.SPIKE_UP));
        traps.add(new Trap(520, 560, 120, 20, Trap.TrapType.SPIKE_UP));

    }
}
