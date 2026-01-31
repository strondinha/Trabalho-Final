package com.trollgame.states;

import com.trollgame.core.Game;
import com.trollgame.ui.FadeEffect;

import java.awt.*;

public class TransitionState extends GameState {

    private FadeEffect fadeEffect;
    private int targetState;
    private boolean fadedOut;

    public TransitionState(GameStateManager stateManager) {
        super(stateManager);
        this.targetState = GameStateManager.PLAY_STATE;
    }

    @Override
    public void init() {
        fadeEffect = new FadeEffect();
        fadeEffect.setFadeSpeed(0.04f);
        fadeEffect.startFadeOut();
        fadedOut = false;
    }

    public void setTargetState(int targetState) {
        this.targetState = targetState;
    }

    @Override
    public void update() {
        fadeEffect.update();

        if (fadeEffect.isComplete()) {
            if (!fadedOut) {
                fadedOut = true;
                stateManager.setState(targetState);
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(new Color(255, 248, 235));
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        fadeEffect.render(g);
    }

    @Override
    public void keyPressed(int keyCode) { }

    @Override
    public void keyReleased(int keyCode) { }
}
