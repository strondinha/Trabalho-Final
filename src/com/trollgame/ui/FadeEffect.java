package com.trollgame.ui;

import com.trollgame.core.Game;
import java.awt.*;

/**
 * FadeEffect - Handles fade in and fade out transitions
 */
public class FadeEffect {
    private float alpha;
    private float fadeSpeed;
    private boolean fadingIn;
    private boolean fadingOut;
    private boolean complete;

    public FadeEffect() {
        this.alpha = 0f;
        this.fadeSpeed = 0.04f;
        this.fadingIn = false;
        this.fadingOut = false;
        this.complete = false;
    }

    public void startFadeOut() {
        alpha = 0f;
        fadingOut = true;
        fadingIn = false;
        complete = false;
    }

    public void startFadeIn() {
        alpha = 1f;
        fadingIn = true;
        fadingOut = false;
        complete = false;
    }

    public void update() {
        if (fadingOut) {
            alpha += fadeSpeed * (1 + alpha);
            if (alpha >= 1f) {
                alpha = 1f;
                fadingOut = false;
                complete = true;
            }
        } else if (fadingIn) {
            alpha -= fadeSpeed * (1 + (1 - alpha));
            if (alpha <= 0f) {
                alpha = 0f;
                fadingIn = false;
                complete = true;
            }
        }
    }

    public void render(Graphics2D g) {
        if (alpha > 0) {
            int vignetteAlpha = (int)(alpha * 30);
            if (vignetteAlpha > 0) {
                RadialGradientPaint vignette = new RadialGradientPaint(
                    Game.WIDTH / 2f, Game.HEIGHT / 2f,
                    Game.WIDTH * 0.7f,
                    new float[]{0.0f, 1.0f},
                    new Color[]{
                        new Color(0, 0, 0, 0),
                        new Color(0, 0, 0, vignetteAlpha)
                    }
                );
                g.setPaint(vignette);
                g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
            }

            g.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
            g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        }
    }

    public boolean isComplete() { return complete; }
    public boolean isFading() { return fadingIn || fadingOut; }
    public boolean isFadingOut() { return fadingOut; }
    public boolean isFadingIn() { return fadingIn; }
    public void setFadeSpeed(float speed) { this.fadeSpeed = speed; }
    public float getAlpha() { return alpha; }

    public void reset() {
        alpha = 0f;
        fadingIn = false;
        fadingOut = false;
        complete = false;
    }
}
