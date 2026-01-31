package com.trollgame.ui;

import com.trollgame.core.Game;

import java.awt.*;
import java.util.Random;

public class MatrixBackground {
    private static class MatrixChar { int x; float y, speed; char v; }

    private final MatrixChar[] chars;
    private final Random rnd = new Random();
    private int tick = 0;

    public MatrixBackground() {
        int cols = Game.WIDTH / 20;
        chars = new MatrixChar[cols];
        for (int i = 0; i < cols; i++) {
            MatrixChar c = new MatrixChar();
            c.x = i * 20;
            c.y = rnd.nextInt(Game.HEIGHT);
            c.speed = 1 + rnd.nextFloat() * 3;
            c.v = (char)(33 + rnd.nextInt(60));
            chars[i] = c;
        }
    }

    public void update() {
        tick++;
        for (MatrixChar c : chars) {
            c.y += c.speed;
            if (c.y > Game.HEIGHT) c.y = -20;
            if (tick % 20 == 0) c.v = (char)(33 + rnd.nextInt(60));
        }
    }

    public void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        g.setColor(new Color(0, 255, 0, 120));
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        for (MatrixChar c : chars) {
            g.drawString(String.valueOf(c.v), c.x, (int)c.y);
        }
    }
}
