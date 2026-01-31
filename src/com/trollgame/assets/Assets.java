package com.trollgame.assets;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public final class Assets {
    public static final String[] SKIN_NAMES = {"Azul", "Fogo", "Tóxico", "Raio"};
    private static final BufferedImage[] skins = new BufferedImage[4];
    private static boolean loaded = false;

    private Assets() {}

    public static void load() {
        if (loaded) return;
        loaded = true;

        String[] files = {"blue", "red", "green", "yellow"};
        for (int i = 0; i < skins.length; i++) {
            try {
                skins[i] = ImageIO.read(Assets.class.getResource(
                    "/res/player/boy_" + files[i] + ".png"
                ));
            } catch (Exception e) {
                skins[i] = null;
            }
        }
    }

    public static BufferedImage getSkin(int index) {
        load();
        if (index < 0 || index >= skins.length) return null;
        return skins[index];
    }
}
