package mods.thecomputerizer.dimhoppertweaks.mixin.mods.mobends;

import goblinbob.mobends.core.client.event.EntityRenderHandler;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderHandler.class, remap = false)
public class MixinEntityRenderHandler {

    @Inject(at = @At("HEAD"), method = "beforeLivingRender", cancellable = true)
    private void dimhoppertweaks$onPreRenderCheck(RenderLivingEvent.Pre<? extends EntityLivingBase> event, CallbackInfo ci) {
        if(DelayedModAccess.isFakeEntity(event.getEntity())) ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "afterLivingRender", cancellable = true)
    private void dimhoppertweaks$onPostRenderCheck(RenderLivingEvent.Post<? extends EntityLivingBase> event, CallbackInfo ci) {
        if(DelayedModAccess.isFakeEntity(event.getEntity())) ci.cancel();
    }
}
