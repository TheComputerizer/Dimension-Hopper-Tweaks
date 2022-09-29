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

    private void setHoverIndex(int mouseX, int mouseY) {
        if(this.menuShowing) {
            if(mouseX>=this.x && mouseX<=this.x+this.width && mouseY>=this.y+height && mouseY<=this.y+(height*this.menuElements.size())) {
                int menuHeight = this.y+(height*this.menuElements.size())-this.y+height;
                int adjustedMouseHeight = mouseY-this.y+height;
                this.hoverIndex = (int)(((double)menuHeight/(double)adjustedMouseHeight)*(double)this.menuElements.size());
            } else this.hoverIndex = -1;
        } else this.hoverIndex = -1;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
        super.drawButton(mc, mouseX, mouseY, partial);
        if(this.menuShowing) {
            int yPos = this.y+this.height;
            int color = 14737632;
            if (packedFGColour != 0) color = packedFGColour;
            else if (this.hovered) color = 16777120;
            for(String element : this.menuElements) {
                this.drawCenteredString(mc.fontRenderer,element,this.x,yPos,color);
            }

        }
    }
}
