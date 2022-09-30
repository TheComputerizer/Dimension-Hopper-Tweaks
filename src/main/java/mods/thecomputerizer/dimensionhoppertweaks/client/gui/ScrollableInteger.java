package mods.thecomputerizer.dimensionhoppertweaks.client.gui;

import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.input.Mouse;

public class ScrollableInteger extends GuiButtonExt {

    public ScrollableInteger(int id, int xPos, int yPos, int width, int height, String displayString, int originalNumber) {
        super(id, xPos, yPos, width, height, displayString);
    }

    public int handleScroll(int original) {
        int ret = translateScroll(original);
        this.displayString = ""+ret;
        return ret;
    }

    private int translateScroll(int original) {
        int mouseScroll = Mouse.getEventDWheel();
        if (mouseScroll == 0 || !this.hovered) return original;
        if (mouseScroll > 0) return Math.min(100, original + 1);
        return Math.max(1, original - 1);
    }
}
