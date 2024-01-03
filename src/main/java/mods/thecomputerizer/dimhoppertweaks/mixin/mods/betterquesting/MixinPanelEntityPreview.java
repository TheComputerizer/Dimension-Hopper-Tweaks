package mods.thecomputerizer.dimhoppertweaks.mixin.mods.betterquesting;

import betterquesting.api2.client.gui.misc.IGuiRect;
import betterquesting.api2.client.gui.panels.content.PanelEntityPreview;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = PanelEntityPreview.class, remap = false)
public class MixinPanelEntityPreview {

    @Inject(at = @At("RETURN"), method = "<init>")
    private void dimhoppertweaks$afterInit(IGuiRect rect, Entity entity, CallbackInfo ci) {
        if(Objects.nonNull(entity)) entity.getEntityData().setBoolean("isFakeEntityForMoBends",true);
    }
}
