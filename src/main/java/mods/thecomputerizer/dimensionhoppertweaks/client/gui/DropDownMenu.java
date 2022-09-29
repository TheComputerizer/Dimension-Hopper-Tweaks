package mods.thecomputerizer.dimensionhoppertweaks.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.util.List;

public class DropDownMenu extends GuiButtonExt {

    private final List<String> menuElements;
    private boolean menuShowing;
    private int hoverIndex;

    public DropDownMenu(int id, int xPos, int yPos, int width, int height, String displayString, List<String> elements) {
        super(id, xPos, yPos, width, height, displayString);
        this.menuElements = elements;
        this.menuShowing = false;
        this.hoverIndex = -1;
    }

    public void setMenu(boolean showing) {
        this.menuShowing = showing;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.setMenu(!this.menuShowing);
    }

    public boolean checkForMenuUnderMouse(int mouseX, int mouseY) {
        return mouseX>=this.x && mouseX<=this.x+this.width && mouseY>=this.y+height && mouseY<=this.y+(height*this.menuElements.size());
    }

    private void setHoverIndex(int mouseX, int mouseY) {
        if(this.menuShowing) {
            if(checkForMenuUnderMouse(mouseX, mouseY)) {
                int menuHeight = this.y+(height*this.menuElements.size())-this.y+height;
                int adjustedMouseHeight = mouseY-this.y+height;
                this.hoverIndex = (int)(((double)menuHeight/(double)adjustedMouseHeight)*(double)this.menuElements.size());
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
            int yPos = this.y+this.height;
            int color = 14737632;
            if (packedFGColour != 0) color = packedFGColour;
            for(int i=0;i<this.menuElements.size();i++) {
                String element = this.menuElements.get(i);
                this.drawCenteredString(mc.fontRenderer,element,this.x,yPos,checkAndSetHoverColor(color,i));
                yPos+=this.height;
            }
        }
    }
}
