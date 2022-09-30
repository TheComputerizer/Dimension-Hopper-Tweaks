package mods.thecomputerizer.dimensionhoppertweaks.client.gui;

import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.input.Mouse;

import java.util.List;

public class ScrollableList extends GuiButtonExt {

    private final List<String> originalElements;
    private final List<String> translatedElements;
    private int index;

    public ScrollableList(int id, int xPos, int yPos, int width, int height, String displayString,
                          List<String> originalElements, List<String> translatedElements) {
        super(id, xPos, yPos, width, height, displayString);
        this.originalElements = originalElements;
        this.translatedElements = translatedElements;
        this.index = 0;
    }

    public String handleScroll() {
        this.index = translateScroll(this.index);
        this.displayString = this.translatedElements.get(this.index);
        return this.originalElements.get(this.index);
    }

    private int translateScroll(int original) {
        int mouseScroll = Mouse.getEventDWheel();
        if (mouseScroll == 0 || !this.hovered) return original;
        if (mouseScroll > 0) {
            original++;
            if(original>=this.originalElements.size()) original-=this.originalElements.size();
            return original;
        };
        original--;
        if(original<0) original+=this.originalElements.size();
        return original;
    }
}
