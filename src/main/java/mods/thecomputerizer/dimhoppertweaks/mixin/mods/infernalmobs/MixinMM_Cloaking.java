package mods.thecomputerizer.dimhoppertweaks.mixin.mods.infernalmobs;

import atomicstryker.infernalmobs.common.MobModifier;
import atomicstryker.infernalmobs.common.mods.MM_Cloaking;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MM_Cloaking.class, remap = false)
public abstract class MixinMM_Cloaking extends MobModifier {
    
    @Inject(at = @At("HEAD"), method = "onUpdate", cancellable = true)
    private void dimhoppertweaks$onUpdate(EntityLivingBase mob, CallbackInfoReturnable<Boolean> cir) {
        if(DelayedModAccess.isInfernalDistracted(getMobTarget())) cir.setReturnValue(false);
    }
    
    @Inject(at = @At("HEAD"), method = "onHurt", cancellable = true)
    private void dimhoppertweaks$onHurt(EntityLivingBase entity, DamageSource source, float damage,
            CallbackInfoReturnable<Float> cir) {
        if(DelayedModAccess.isInfernalDistracted(getMobTarget())) cir.setReturnValue(damage);
    }
}