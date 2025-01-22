package mods.thecomputerizer.dimhoppertweaks.mixin.mods.mobends;

import goblinbob.mobends.standard.client.renderer.entity.layers.LayerCustomCape;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(value = LayerCustomCape.class, remap = false)
public abstract class MixinLayerCustomCape {

    @Inject(at = @At("HEAD"), method = "doRenderLayer(Lnet/minecraft/client/entity/AbstractClientPlayer;FFFFFFF)V",
            remap = false, cancellable = true)
    private void dimhoppertweaks$doRenderLayer(
            AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
            float netHeadYaw, float headPitch, float scale, CallbackInfo ci) {
        if(DelayedModAccess.isFakeEntity(player)) ci.cancel();
    }
}