package com.trollgame.levels;

import com.trollgame.core.Game;
import com.trollgame.entities.*;
import com.trollgame.events.EventTrigger;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Level - Abstract base class for all game levels
 */
public abstract class Level {
    protected Player player;
    protected Goal goal;
    protected List<Platform> platforms;
    protected List<MovingPlatform> movingPlatforms;
    protected List<Trap> traps;
    protected List<EventTrigger> eventTriggers;

    protected float spawnX, spawnY;
    protected int levelNumber;

    protected int wallThickness = 20;

    protected int worldWidth = Game.WIDTH;
    protected int worldHeight = Game.HEIGHT;
    protected float cameraX = 0;
    protected float cameraY = 0;
    protected float cameraLerp = 0.12f;
    protected boolean cameraEnabled = true;

    protected Color backgroundColor = new Color(255, 248, 235);
    protected Color wallColor = new Color(230, 200, 170);
    protected Color wallBorderColor = new Color(180, 140, 100);
    protected int themeIndex = 0;
    private static final HashMap<Integer, BufferedImage> THEME_BACKGROUNDS = new HashMap<>();

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.platforms = new ArrayList<>();
        this.movingPlatforms = new ArrayList<>();
        this.traps = new ArrayList<>();
        this.eventTriggers = new ArrayList<>();
    }

    public abstract void init();

    public void update() {
        player.update();

        for (Platform p : platforms) {
            p.update();
        }

        for (MovingPlatform mp : movingPlatforms) {
            mp.update();
        }

        for (Trap t : traps) {
            t.update();
        }

        goal.update();

        for (EventTrigger trigger : eventTriggers) {
            trigger.check(player, this);
            if (trigger.isTriggered() && trigger.getEvent().isContinuous()) {
                trigger.getEvent().update(this);
            }
        }

        handleCollisions();
        checkBoundaries();
        updateCamera();
    }

    public void render(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(-cameraX, -cameraY);

        drawBackground(g2);
        drawWalls(g2);

        goal.render(g2);

        for (Platform p : platforms) {
            p.render(g2);
        }

        for (MovingPlatform mp : movingPlatforms) {
            mp.render(g2);
        }

        for (Trap t : traps) {
            t.render(g2);
        }

        player.render(g2);
        g2.dispose();
    }

    protected void drawBackground(Graphics2D g) {
        BufferedImage themed = loadThemeBackground(themeIndex);
        if (themed != null) {
            g.drawImage(themed, 0, 0, worldWidth, worldHeight, null);
            drawThemedOverlay(g);
            return;
        }

        Color top = backgroundColor.brighter();
        Color bottom = backgroundColor.darker();
        GradientPaint bg = new GradientPaint(0, 0, top, 0, worldHeight, bottom);
        g.setPaint(bg);
        g.fillRect(0, 0, worldWidth, worldHeight);

        switch (themeIndex) {
            case 0: // Floresta
                g.setColor(new Color(255, 240, 180, 90));
                g.fillOval(40, 40, 120, 120); // sol
                g.setColor(new Color(20, 80, 40, 140));
                for (int i = 0; i < 10; i++) {
                    int x = i * 90;
                    g.fillOval(x, 360, 160, 120);
                }
                g.setColor(new Color(10, 60, 30, 160));
                for (int i = 0; i < 12; i++) {
                    int x = i * 70;
                    g.fillRect(x, 420, 30, 180);
                }
                g.setColor(new Color(255, 255, 255, 40));
                g.fillRect(0, 420, worldWidth, 70);
                break;
            case 1: // Gelo
                g.setColor(new Color(220, 240, 255, 90));
                g.fillOval(560, 40, 140, 140); // lua
                g.setColor(new Color(255, 255, 255, 60));
                for (int i = 0; i < 80; i++) {
                    int x = (i * 43) % worldWidth;
                    int y = (i * 29) % worldHeight;
                    g.fillRect(x, y, 2, 2);
                }
                g.setColor(new Color(170, 210, 255, 80));
                g.fillPolygon(new int[]{-50, 120, 260}, new int[]{500, 340, 520}, 3);
                g.fillPolygon(new int[]{200, 380, 560}, new int[]{520, 330, 520}, 3);
                g.fillPolygon(new int[]{480, 640, 840}, new int[]{520, 340, 520}, 3);
                break;
            case 2: // Deserto
                g.setColor(new Color(255, 220, 120, 110));
                g.fillOval(580, 40, 140, 140); // sol forte
                g.setColor(new Color(230, 170, 80, 140));
                g.fillOval(-100, 420, 340, 130);
                g.fillOval(200, 450, 360, 130);
                g.fillOval(520, 430, 340, 130);
                g.setColor(new Color(255, 230, 160, 60));
                g.fillRect(0, 300, 800, 200);
                g.setColor(new Color(200, 150, 80, 120));
                g.fillRect(80, 380, 12, 60); // cacto
                g.fillRect(88, 400, 20, 10);
                g.fillRect(92, 360, 10, 20);
                break;
            case 3: // Vulcão
                g.setColor(new Color(80, 20, 15, 160));
                g.fillRect(0, 320, 800, 280);
                g.setColor(new Color(255, 80, 40, 80));
                for (int i = 0; i < 12; i++) {
                    int x = (i * 70) % worldWidth;
                    g.fillOval(x, 360, 120, 60);
                }
                g.setColor(new Color(255, 120, 60, 120));
                g.fillPolygon(new int[]{140, 260, 380}, new int[]{520, 340, 520}, 3);
                g.fillPolygon(new int[]{420, 560, 700}, new int[]{520, 320, 520}, 3);
                break;
            default: // Místico
                g.setColor(new Color(255, 255, 255, 70));
                for (int i = 0; i < 120; i++) {
                    int x = (i * 37) % worldWidth;
                    int y = (i * 23) % worldHeight;
                    g.fillRect(x, y, 2, 2);
                }
                g.setColor(new Color(180, 120, 255, 90));
                g.fillOval(80, 360, 240, 100);
                g.fillOval(380, 380, 300, 120);
                g.setColor(new Color(100, 60, 140, 120));
                g.fillOval(520, 60, 160, 100); // névoa
                break;
        }
    }

    private BufferedImage loadThemeBackground(int index) {
        if (THEME_BACKGROUNDS.containsKey(index)) {
            return THEME_BACKGROUNDS.get(index);
        }
        String path = "/res/backgrounds/theme_" + index + ".png";
        try {
            BufferedImage img = ImageIO.read(getClass().getResource(path));
            THEME_BACKGROUNDS.put(index, img);
            return img;
        } catch (Exception e) {
            THEME_BACKGROUNDS.put(index, null);
            return null;
        }
    }

    private void drawThemedOverlay(Graphics2D g) {
        // Camada sutil para disfarçar marca d'água e dar vida ao fundo
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.18f));
        g.setColor(Color.BLACK);
        g.fillRect(worldWidth - 70, worldHeight - 70, 70, 70);

        // Efeito fosco suave
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.10f));
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, worldWidth, worldHeight);

        // Partículas leves
        long t = System.currentTimeMillis() / 20;
        Color glow = getThemeGlowColor();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        g.setColor(glow);
        for (int i = 0; i < 24; i++) {
            int x = (int) ((i * 97 + t) % worldWidth);
            int y = (int) ((i * 53 + (t * 2)) % worldHeight);
            g.fillOval(x, y, 3, 3);
        }

        // Vinheta leve nas bordas
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.12f));
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, worldWidth, 20);
        g.fillRect(0, worldHeight - 20, worldWidth, 20);
        g.fillRect(0, 0, 20, worldHeight);
        g.fillRect(worldWidth - 20, 0, 20, worldHeight);

        g.setComposite(AlphaComposite.SrcOver);
    }

    private Color getThemeGlowColor() {
        switch (themeIndex) {
            case 0: return new Color(180, 140, 255); // galáxia
            case 1: return new Color(220, 240, 255); // céu
            case 2: return new Color(160, 220, 140); // natureza
            case 3: return new Color(255, 140, 80); // lava
            default: return new Color(120, 220, 255); // cyber/místico
        }
    }

    protected void drawWalls(Graphics2D g) {
        g.setColor(wallColor);

        g.fillRect(0, 0, worldWidth, wallThickness);
        g.fillRect(0, 0, wallThickness, worldHeight);
        g.fillRect(worldWidth - wallThickness, 0, wallThickness, worldHeight);

        g.setColor(wallBorderColor);
        g.setStroke(new BasicStroke(2));
        g.drawRect(wallThickness, wallThickness, worldWidth - 2 * wallThickness, worldHeight - 3 * wallThickness);
    }

    protected void handleCollisions() {
        Rectangle playerBounds = player.getBounds();
        player.setOnGround(false);

        for (Platform p : platforms) {
            if (p.isSolid()) {
                handlePlatformCollision(player, p);
            }
        }

        for (MovingPlatform mp : movingPlatforms) {
            if (mp.isSolid()) {
                handleMovingPlatformCollision(player, mp);
            }
        }

        for (Trap t : traps) {
            if (t.isActive() && playerBounds.intersects(t.getBounds())) {
                player.kill();
                return;
            }
        }

        handleWallCollisions();
    }

    protected void handlePlatformCollision(Player player, Platform platform) {
        Rectangle playerBounds = player.getBounds();
        Rectangle platBounds = platform.getBounds();

        if (!playerBounds.intersects(platBounds)) return;

        float playerBottom = player.getY() + player.getHeight();
        float playerTop = player.getY();
        float playerLeft = player.getX();
        float playerRight = player.getX() + player.getWidth();

        float platTop = platform.getY();
        float platBottom = platform.getY() + platform.getHeight();
        float platLeft = platform.getX();
        float platRight = platform.getX() + platform.getWidth();

        float overlapTop = playerBottom - platTop;
        float overlapBottom = platBottom - playerTop;
        float overlapLeft = playerRight - platLeft;
        float overlapRight = platRight - playerLeft;

        float minOverlap = Math.min(Math.min(overlapTop, overlapBottom), Math.min(overlapLeft, overlapRight));

        if (minOverlap == overlapTop && player.getVelocityY() >= 0) {
            player.setY(platTop - player.getHeight());
            player.setVelocityY(0);
            player.setOnGround(true);
        } else if (minOverlap == overlapBottom && player.getVelocityY() < 0) {
            player.setY(platBottom);
            player.setVelocityY(0);
            if (player.isGravityInverted()) {
                player.setOnGround(true);
            }
        } else if (minOverlap == overlapLeft) {
            player.setX(platLeft - player.getWidth());
        } else if (minOverlap == overlapRight) {
            player.setX(platRight);
        }
    }

    protected void handleMovingPlatformCollision(Player player, MovingPlatform platform) {
        Rectangle playerBounds = player.getBounds();
        Rectangle platBounds = platform.getBounds();

        boolean onPlatform = player.getY() + player.getHeight() >= platform.getY() - 5 &&
                            player.getY() + player.getHeight() <= platform.getY() + 10 &&
                            player.getX() + player.getWidth() > platform.getX() &&
                            player.getX() < platform.getX() + platform.getWidth();

        if (onPlatform && player.getVelocityY() >= 0) {
            player.setY(platform.getY() - player.getHeight());
            player.setVelocityY(0);
            player.setOnGround(true);

            player.setX(player.getX() + platform.getDeltaX());
        } else {
            handlePlatformCollision(player, platform);
        }
    }

    protected void handleWallCollisions() {
        if (player.getX() < wallThickness) {
            player.setX(wallThickness);
        }
        if (player.getX() + player.getWidth() > worldWidth - wallThickness) {
            player.setX(worldWidth - wallThickness - player.getWidth());
        }
        if (player.getY() < wallThickness) {
            player.setY(wallThickness);
            if (!player.isGravityInverted()) {
                player.setVelocityY(0);
            } else {
                player.setOnGround(true);
                player.setVelocityY(0);
            }
        }
        // Sem chão: cair é fatal
    }

    protected void checkBoundaries() {
        if (player.getY() > worldHeight + 100) {
            player.kill();
        }
    }

    protected void updateCamera() {
        if (!cameraEnabled) {
            cameraX = 0;
            cameraY = 0;
            return;
        }

        float targetX = player.getX() + player.getWidth() / 2f - Game.WIDTH / 2f;
        float targetY = player.getY() + player.getHeight() / 2f - Game.HEIGHT / 2f;

        targetX = Math.max(0, Math.min(targetX, worldWidth - Game.WIDTH));
        targetY = Math.max(0, Math.min(targetY, worldHeight - Game.HEIGHT));

        cameraX += (targetX - cameraX) * cameraLerp;
        cameraY += (targetY - cameraY) * cameraLerp;
    }

    public void setWorldSize(int width, int height) {
        this.worldWidth = Math.max(Math.max(this.worldWidth, width), Game.WIDTH);
        this.worldHeight = Math.max(Math.max(this.worldHeight, height), Game.HEIGHT);
    }

    public void expandLevelSpacing(float factor) {
        if (factor <= 1f) return;

        float origin = spawnX;
        float maxX = 0;

        for (Platform p : platforms) {
            float nx = origin + (p.getX() - origin) * factor;
            p.setX(nx);
            maxX = Math.max(maxX, nx + p.getWidth());
        }

        for (MovingPlatform mp : movingPlatforms) {
            mp.scaleFromOriginX(origin, factor);
            maxX = Math.max(maxX, Math.max(mp.getStartX(), mp.getEndX()) + mp.getWidth());
        }

        for (Trap t : traps) {
            float nx = origin + (t.getX() - origin) * factor;
            t.setX(nx);
            maxX = Math.max(maxX, nx + t.getWidth());
        }

        if (goal != null) {
            float nx = origin + (goal.getX() - origin) * factor;
            goal.setX(nx);
            maxX = Math.max(maxX, nx + goal.getWidth());
        }

        for (EventTrigger trigger : eventTriggers) {
            trigger.scaleFromOriginX(origin, factor);
        }

        worldWidth = Math.max(worldWidth, (int)maxX + 200);
    }

    public void setCameraEnabled(boolean enabled) {
        this.cameraEnabled = enabled;
        if (!enabled) {
            this.cameraX = 0;
            this.cameraY = 0;
        }
    }
    public boolean isCameraEnabled() { return cameraEnabled; }
    public void toggleCameraMode() {
        this.cameraEnabled = !this.cameraEnabled;
        if (!this.cameraEnabled) {
            this.cameraX = 0;
            this.cameraY = 0;
        }
    }

    public void reset() {
        player.respawn(spawnX, spawnY);

        for (Platform p : platforms) {
            p.appear();
        }

        for (MovingPlatform mp : movingPlatforms) {
            mp.appear();
            mp.resetSpeed();
        }

        traps.removeIf(t -> !isOriginalTrap(t));
        for (Trap t : traps) {
            resetTrap(t);
        }

        for (EventTrigger trigger : eventTriggers) {
            trigger.reset();
        }

        resetLevelState();
    }

    protected boolean isOriginalTrap(Trap t) {
        return true;
    }

    protected void resetTrap(Trap t) {
        // Override in subclasses
    }

    protected void resetLevelState() {
        // Override in subclasses
    }

    public void applyTheme(int themeIndex) {
        this.themeIndex = themeIndex;
        switch (themeIndex) {
            case 0:
                backgroundColor = new Color(120, 210, 140); // Floresta
                wallColor = new Color(60, 120, 80);
                wallBorderColor = new Color(20, 80, 50);
                break;
            case 1:
                backgroundColor = new Color(120, 180, 255); // Gelo
                wallColor = new Color(80, 130, 200);
                wallBorderColor = new Color(30, 80, 140);
                break;
            case 2:
                backgroundColor = new Color(245, 210, 120); // Deserto
                wallColor = new Color(200, 140, 60);
                wallBorderColor = new Color(140, 90, 30);
                break;
            case 3:
                backgroundColor = new Color(200, 70, 50); // Vulcão
                wallColor = new Color(120, 30, 20);
                wallBorderColor = new Color(70, 15, 10);
                break;
            default:
                backgroundColor = new Color(180, 120, 220); // Místico
                wallColor = new Color(90, 50, 130);
                wallBorderColor = new Color(50, 20, 90);
                break;
        }
    }

    public boolean isComplete() {
        return player.getBounds().intersects(goal.getBounds());
    }

    public boolean isPlayerDead() { return !player.isAlive(); }

    public Player getPlayer() { return player; }
    public Goal getGoal() { return goal; }
    public List<Platform> getPlatforms() { return platforms; }
    public List<MovingPlatform> getMovingPlatforms() { return movingPlatforms; }
    public List<Trap> getTraps() { return traps; }
    public List<EventTrigger> getEventTriggers() { return eventTriggers; }
    public int getLevelNumber() { return levelNumber; }
}
