package mods.thecomputerizer.dimensionhoppertweaks.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.util.List;

public class DropDownMenu extends GuiButtonExt {

    private final List<String> originalElements;
    private final List<String> translatedElements;
    private boolean menuShowing;
    private int hoverIndex;

    public DropDownMenu(int id, int xPos, int yPos, int width, int height, String displayString,
                        List<String> originalElements, List<String> translatedElements) {
        super(id, xPos, yPos, width, height, displayString);
        this.originalElements = originalElements;
        this.translatedElements = translatedElements;
        this.menuShowing = false;
        this.hoverIndex = -1;
    }

    public void setMenu(boolean showing) {
        this.menuShowing = showing;
    }

    public String updateCurrentElement(String original) {
        if(this.hoverIndex==-1 && this.menuShowing) this.menuShowing = false;
        else this.displayString = this.translatedElements.get(this.hoverIndex);
        return this.hoverIndex==1 ? original : this.originalElements.get(this.hoverIndex);
    }

    public boolean checkForMenuUnderMouse(int mouseX, int mouseY) {
        return mouseX>=this.x && mouseX<=this.x+this.width && mouseY>=this.y+height && mouseY<=this.y+(height*this.originalElements.size());
    }

    private void setHoverIndex(int mouseX, int mouseY) {
        if(this.menuShowing) {
            if(checkForMenuUnderMouse(mouseX, mouseY)) {
                int menuHeight = this.y+(height*this.originalElements.size())-this.y+height;
                int adjustedMouseHeight = mouseY-this.y+height;
                this.hoverIndex = (int)(((double)menuHeight/(double)adjustedMouseHeight)*(double)this.originalElements.size());
            } else this.hoverIndex = -1;
        } else this.hoverIndex = -1;
    }

    private int checkAndSetHoverColor(int initialColor, int hoverIndex) {
        if(this.hoverIndex==hoverIndex) return 16777120;
        return initialColor;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
        super.drawButton(mc, mouseX, mouseY, partial);
        if(this.menuShowing) {
            setHoverIndex(mouseX, mouseY);
            int yPos = this.y+this.height;
            int color = 14737632;
            if (packedFGColour != 0) color = packedFGColour;
            for(int i=0;i<this.originalElements.size();i++) {
                String element = this.originalElements.get(i);
                this.drawCenteredString(mc.fontRenderer,element,this.x,yPos,checkAndSetHoverColor(color,i));
                yPos+=this.height;
            }
        }
    }
}
