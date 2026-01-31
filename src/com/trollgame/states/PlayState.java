package com.trollgame.states;

import com.trollgame.assets.Assets;
import com.trollgame.core.Game;
import com.trollgame.core.SoundManager;
import com.trollgame.levels.Level;
import com.trollgame.levels.LevelFactory;
import com.trollgame.ui.FadeEffect;
import com.trollgame.ui.HUD;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayState extends GameState {

    private Level currentLevel;
    private HUD hud;
    private FadeEffect fadeEffect;

    private boolean paused;
    private boolean levelComplete;
    private boolean dying;

    private int deathTimer;
    private static final int DEATH_DELAY = 60;
    private long lastTimeCheck;
    private double levelTime;
    private boolean timeCommitted;
    private boolean deathSfxPlayed;
    private boolean winSfxPlayed;

    public PlayState(GameStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void init() {
        int levelNum = stateManager.getCurrentLevel();
        currentLevel = LevelFactory.createLevel(levelNum);

        currentLevel.getPlayer().setSprite(Assets.getSkin(stateManager.getSelectedSkin()));

        hud = new HUD();
        hud.setCurrentLevel(levelNum);
        hud.setPlayerName(stateManager.getPlayerName());

        fadeEffect = new FadeEffect();
        fadeEffect.startFadeIn();

        SoundManager.loopMusicWithFallback(getThemeMusicPath(levelNum), "/res/sound/theme_0.wav");

        paused = false;
        levelComplete = false;
        dying = false;
        deathTimer = 0;
        levelTime = 0;
        timeCommitted = false;
        lastTimeCheck = System.nanoTime();
        deathSfxPlayed = false;
        winSfxPlayed = false;
    }

    @Override
    public void update() {
        fadeEffect.update();

        if (fadeEffect.isFadingIn()) {
            return;
        }

        if (dying) {
            if (!deathSfxPlayed) {
                SoundManager.playSfx("/res/sound/die.wav");
                deathSfxPlayed = true;
            }
            deathTimer++;
            if (fadeEffect.isComplete() && deathTimer >= DEATH_DELAY / 2) {
                currentLevel.reset();
                dying = false;
                deathTimer = 0;
                levelTime = 0;
                lastTimeCheck = System.nanoTime();
                deathSfxPlayed = false;
                fadeEffect.startFadeIn();
            } else if (!fadeEffect.isFading() && deathTimer < DEATH_DELAY / 2) {
                fadeEffect.startFadeOut();
            }
            return;
        }

        if (levelComplete) {
            if (!winSfxPlayed) {
                SoundManager.playSfx("/res/sound/levelup.wav");
                winSfxPlayed = true;
            }
            if (fadeEffect.isComplete()) {
                if (!timeCommitted) {
                    stateManager.addToTotalTime(levelTime);
                    timeCommitted = true;
                }
                if (LevelFactory.hasNextLevel(stateManager.getCurrentLevel())) {
                    stateManager.unlockLevel(stateManager.getCurrentLevel() + 1);
                    stateManager.nextLevel();
                    stateManager.transitionTo(GameStateManager.MAP_STATE);
                } else {
                    com.trollgame.scores.ScoreManager.saveScore(
                        stateManager.getPlayerName(),
                        stateManager.getTotalTime(),
                        Assets.SKIN_NAMES[stateManager.getSelectedSkin()]
                    );
                    stateManager.transitionTo(GameStateManager.VICTORY_STATE);
                }
            } else if (!fadeEffect.isFading()) {
                fadeEffect.startFadeOut();
            }
            return;
        }

        if (!paused) {
            double delta = (System.nanoTime() - lastTimeCheck) / 1e9;
            levelTime += delta;
            lastTimeCheck = System.nanoTime();
            currentLevel.update();
            hud.update();
            hud.setLevelTime(levelTime);

            if (currentLevel.isPlayerDead()) {
                dying = true;
                hud.incrementDeaths();
                fadeEffect.startFadeOut();
            }

            if (currentLevel.isComplete()) {
                levelComplete = true;
            }
        } else {
            lastTimeCheck = System.nanoTime();
        }
    }

    @Override
    public void render(Graphics2D g) {
        currentLevel.render(g);
        hud.render(g);

        if (paused) {
            renderPauseOverlay(g);
        }

        if (dying) {
            renderDeathMessage(g);
        }

        if (levelComplete) {
            renderCompleteMessage(g);
        }

        fadeEffect.render(g);
    }

    private void renderPauseOverlay(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String text = "PAUSADO";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, (Game.WIDTH - fm.stringWidth(text)) / 2, Game.HEIGHT / 2);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String subtext = "Pressione ESC para continuar";
        fm = g.getFontMetrics();
        g.drawString(subtext, (Game.WIDTH - fm.stringWidth(subtext)) / 2, Game.HEIGHT / 2 + 40);

        String subtext2 = "Pressione M para ir ao menu";
        fm = g.getFontMetrics();
        g.drawString(subtext2, (Game.WIDTH - fm.stringWidth(subtext2)) / 2, Game.HEIGHT / 2 + 70);
    }

    private void renderDeathMessage(Graphics2D g) {
        g.setColor(new Color(200, 60, 60));
        g.setFont(new Font("Arial", Font.BOLD, 32));
        String text = "VOCÊ PERDEU!";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, (Game.WIDTH - fm.stringWidth(text)) / 2, Game.HEIGHT / 2);
    }

    private void renderCompleteMessage(Graphics2D g) {
        g.setColor(new Color(100, 180, 100));
        g.setFont(new Font("Arial", Font.BOLD, 32));
        String text = "FASE CONCLUÍDA!";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, (Game.WIDTH - fm.stringWidth(text)) / 2, Game.HEIGHT / 2);
    }

    @Override
    public void keyPressed(int keyCode) {
        if (dying || levelComplete) return;

        switch (keyCode) {
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
                if (!paused) {
                    lastTimeCheck = System.nanoTime();
                }
                break;

            case KeyEvent.VK_M:
                if (paused) {
                    paused = false;
                    stateManager.transitionTo(GameStateManager.MENU_STATE);
                }
                break;

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                if (!paused) currentLevel.getPlayer().setMovingLeft(true);
                break;

            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                if (!paused) currentLevel.getPlayer().setMovingRight(true);
                break;

            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (!paused) currentLevel.getPlayer().jump();
                if (!paused) SoundManager.playSfx("/res/sound/jump.wav");
                break;

            case KeyEvent.VK_R:
                if (!paused) {
                    dying = true;
                    hud.incrementDeaths();
                }
                break;
            case KeyEvent.VK_C:
                if (!paused) {
                    currentLevel.toggleCameraMode();
                }
                break;
        }
    }

    @Override
    public void keyReleased(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                currentLevel.getPlayer().setMovingLeft(false);
                break;

            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                currentLevel.getPlayer().setMovingRight(false);
                break;

            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                currentLevel.getPlayer().releaseJump();
                break;
        }
    }

    public HUD getHud() { return hud; }

    private String getThemeMusicPath(int levelNum) {
        int themeIndex = (levelNum - 1) / 3;
        switch (themeIndex) {
            case 0: return "/res/sound/theme_0.wav"; // galáxia
            case 1: return "/res/sound/theme_1.wav"; // céu
            case 2: return "/res/sound/theme_2.wav"; // natureza
            case 3: return "/res/sound/theme_3.wav"; // lava
            default: return "/res/sound/theme_4.wav"; // cyber
        }
    }
}
