package mods.thecomputerizer.dimhoppertweaks.mixin.mods.twilightforest;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.TFEventListener;

@Mixin(value = TFEventListener.class, remap = false)
public abstract class MixinTFEventListener {

    @Inject(at = @At("HEAD"), method = "charmOfLife", cancellable = true)
    private static void dimhoppertweaks$charmOfLife(LivingDeathEvent event, CallbackInfo ci) {
        if(DelayedModAccess.hasGameStage(event.getEntity(),"hardcore") && event.getSource()!=DamageSource.FALL)
            ci.cancel();
    }
}