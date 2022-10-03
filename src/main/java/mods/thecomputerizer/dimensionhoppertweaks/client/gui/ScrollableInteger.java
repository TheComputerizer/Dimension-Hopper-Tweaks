package mods.thecomputerizer.dimensionhoppertweaks.client.gui;

import org.lwjgl.input.Mouse;

public class ScrollableInteger extends CircularScrollableElement {

    private int currentLevel;

    public ScrollableInteger(TokenExchangeGui parent, int centerX, int centerY, int radius, int resolution,
                             int conversionRate, String displayString, String hoverKey) {
       super(parent, centerX, centerY, radius, resolution, displayString, hoverKey);
       this.currentLevel = conversionRate;
    }

    @Override
    public void handleScroll() {
        this.currentLevel = translateScroll(this.currentLevel);
        this.setCenterString(""+this.currentLevel);
        this.getParentScreen().setConversionRate(this.currentLevel);
    }

    private int translateScroll(int original) {
        int mouseScroll = Mouse.getEventDWheel();
        if (mouseScroll == 0 || !this.hover) return original;
        if (mouseScroll > 0) return Math.min(100, original + 1);
        return Math.max(1, original - 1);
    }
}
