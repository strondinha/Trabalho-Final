package com.trollgame.ui;

import java.awt.*;

/**
 * Button - UI button with hover effects
 */
public class Button {
    private int x, y, width, height;
    private String text;
    private boolean hovered;
    private float hoverAnimation = 0;
    private float pulseAnimation = 0;

    private Color normalColor = new Color(255, 210, 170);
    private Color normalColorDark = new Color(255, 180, 130);
    private Color hoverColor = new Color(255, 180, 120);
    private Color hoverColorDark = new Color(255, 140, 80);
    private Color textColor = new Color(80, 50, 30);
    private Color textShadow = new Color(255, 255, 255, 80);
    private Color borderColor = new Color(200, 100, 50);
    private Color shadowColor = new Color(0, 0, 0, 40);

    public Button(int x, int y, int width, int height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.hovered = false;
    }

    public void update() {
        float targetHover = hovered ? 1.0f : 0.0f;
        hoverAnimation += (targetHover - hoverAnimation) * 0.2f;

        if (hovered) {
            pulseAnimation += 0.1f;
        }
    }

    public void render(Graphics2D g) {
        int animatedY = y - (int)(hoverAnimation * 4);
        float scale = 1.0f + hoverAnimation * 0.03f;
        int scaledWidth = (int)(width * scale);
        int scaledHeight = (int)(height * scale);
        int scaledX = x - (scaledWidth - width) / 2;
        int scaledY = animatedY - (scaledHeight - height) / 2;

        g.setColor(shadowColor);
        g.fillRoundRect(scaledX + 3, scaledY + 5 + (int)(hoverAnimation * 2), scaledWidth, scaledHeight, 18, 18);

        Color topColor, bottomColor;
        if (hovered) {
            topColor = hoverColor;
            bottomColor = hoverColorDark;
        } else {
            topColor = normalColor;
            bottomColor = normalColorDark;
        }

        GradientPaint gradient = new GradientPaint(
            scaledX, scaledY, topColor,
            scaledX, scaledY + scaledHeight, bottomColor
        );
        g.setPaint(gradient);
        g.fillRoundRect(scaledX, scaledY, scaledWidth, scaledHeight, 18, 18);

        g.setColor(new Color(255, 255, 255, 60));
        g.fillRoundRect(scaledX + 4, scaledY + 4, scaledWidth - 8, scaledHeight / 2 - 4, 12, 12);

        if (hovered) {
            float pulse = (float)(1 + Math.sin(pulseAnimation) * 0.2f);
            g.setColor(new Color(255, 180, 100, (int)(40 * pulse)));
            g.setStroke(new BasicStroke(6));
            g.drawRoundRect(scaledX - 2, scaledY - 2, scaledWidth + 4, scaledHeight + 4, 20, 20);
        }

        g.setColor(borderColor);
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(scaledX, scaledY, scaledWidth, scaledHeight, 18, 18);

        g.setColor(textShadow);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g.getFontMetrics();
        int textX = scaledX + (scaledWidth - fm.stringWidth(text)) / 2;
        int textY = scaledY + (scaledHeight + fm.getAscent() - fm.getDescent()) / 2;
        g.drawString(text, textX + 1, textY + 1);

        g.setColor(textColor);
        g.drawString(text, textX, textY);

        if (hovered) {
            g.setColor(textColor);
            int arrowX = scaledX - 25;
            int arrowY = scaledY + scaledHeight / 2;
            int[] xPoints = {arrowX, arrowX + 12, arrowX};
            int[] yPoints = {arrowY - 8, arrowY, arrowY + 8};
            g.fillPolygon(xPoints, yPoints, 3);
        }
    }

    public boolean contains(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    public void setHovered(boolean hovered) { this.hovered = hovered; }
    public boolean isHovered() { return hovered; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
