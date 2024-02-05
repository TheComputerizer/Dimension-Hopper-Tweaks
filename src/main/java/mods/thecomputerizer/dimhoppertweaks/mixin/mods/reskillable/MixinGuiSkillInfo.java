package mods.thecomputerizer.dimhoppertweaks.mixin.mods.reskillable;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import codersafterdark.reskillable.client.gui.GuiSkillInfo;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static codersafterdark.reskillable.client.gui.GuiSkillInfo.SKILL_INFO_RES;

@Mixin(value = GuiSkillInfo.class, remap = false)
public abstract class MixinGuiSkillInfo extends GuiScreen {

    @Shadow private int guiWidth;
    @Shadow private int guiHeight;
    @Shadow private boolean canPurchase;
    @Shadow private Unlockable hoveredUnlockable;

    /**
     * @author The_Computerizer
     * @reason Handle animated unlockable textures
     */
    @Overwrite
    private void drawUnlockable(PlayerData data, PlayerSkillInfo info, Unlockable unlockable, int mx, int my) {
        int x = this.width/2-this.guiWidth/2+20+unlockable.getX()*28;
        int y = this.height/2-this.guiHeight/2+37+unlockable.getY()*28;
        this.mc.renderEngine.bindTexture(SKILL_INFO_RES);
        boolean unlocked = info.isUnlocked(unlockable);
        int u = 0;
        int v = this.guiHeight;
        if(unlockable.hasSpikes()) u += 26;
        if(unlocked) v += 26;
        GlStateManager.color(1f,1f,1f);
        this.drawTexturedModalRect(x,y,u,v,26,26);
        this.mc.renderEngine.bindTexture(unlockable.getIcon());
        if(unlockable instanceof ExtendedEventsTrait) ((ExtendedEventsTrait)unlockable).draw(x,y);
        else drawModalRectWithCustomSizedTexture(x+5,y+5,0f,0f,16,16,16f,16f);
        if(mx>=x && my>=y && mx<x+26 && my<y+26) {
            this.canPurchase = !unlocked && info.getSkillPoints()>=unlockable.getCost();
            this.hoveredUnlockable = unlockable;
        }

    }
}