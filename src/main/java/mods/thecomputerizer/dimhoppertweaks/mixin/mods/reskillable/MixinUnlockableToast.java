package mods.thecomputerizer.dimhoppertweaks.mixin.mods.reskillable;

import codersafterdark.reskillable.api.toast.AbstractToast;
import codersafterdark.reskillable.api.toast.UnlockableToast;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.toasts.GuiToast;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = UnlockableToast.class, remap = false)
public abstract class MixinUnlockableToast extends AbstractToast {
    
    @Shadow @Final private Unlockable unlockable;
    
    public MixinUnlockableToast(String title, String description) {
        super(title,description);
    }
    
    /**
     * @author The_Computerizer
     * @reason Handle trait icons
     */
    @Overwrite
    protected void renderImage(GuiToast toast) {
        this.bindImage(toast,this.unlockable.getIcon());
        if(this.unlockable instanceof ExtendedEventsTrait) ((ExtendedEventsTrait)this.unlockable).draw(this.x,this.y);
        else Gui.drawModalRectWithCustomSizedTexture(this.x,this.y,0f,0f,16,16,16f,16f);
    }
}