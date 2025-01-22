package mods.thecomputerizer.dimhoppertweaks.client.gui;

import org.lwjgl.input.Mouse;

import java.util.List;

public class ScrollableList extends CircularScrollableElement {

    private final List<String> originalElements;
    private final List<String> translatedElements;
    private int index;

    public ScrollableList(
            TokenExchangeGui parent, int centerX, int centerY, int radius, int resolution, String displayString,
            String originalSkill, List<String> originalElements, List<String> translatedElements, String hoverKey) {
        super(parent,centerX,centerY,radius,resolution,displayString,hoverKey);
        this.originalElements = originalElements;
        this.translatedElements = translatedElements;
        this.index = 0;
        if(originalElements.contains(originalSkill)) this.index = originalElements.indexOf(originalSkill);
    }

    @Override
    public void handleScroll() {
        this.index = translateScroll(this.index);
        this.displayString = this.translatedElements.get(this.index);
        this.getParentScreen().setCurrentSkill(this.originalElements.get(this.index));
    }

    private int translateScroll(int original) {
        int mouseScroll = Mouse.getEventDWheel();
        if(mouseScroll==0 || !getHover()) return original;
        if(mouseScroll>0) {
            original++;
            if(original>=this.originalElements.size()) original-=this.originalElements.size();
            return original;
        }
        original--;
        if(original<0) original+=this.originalElements.size();
        return original;
    }
}