package mods.thecomputerizer.dimhoppertweaks.mixin.mods.infernalmobs;

import atomicstryker.infernalmobs.common.MobModifier;
import atomicstryker.infernalmobs.common.mods.MM_Fiery;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MM_Fiery.class, remap = false)
public abstract class MixinMM_Fiery extends MobModifier {
    
    @Inject(at = @At("HEAD"), method = "onAttack", cancellable = true)
    private void dimhoppertweaks$onAttack(EntityLivingBase entity, DamageSource source, float damage,
            CallbackInfoReturnable<Float> cir) {
        if(DelayedModAccess.isInfernalDistracted(entity)) cir.setReturnValue(damage);
    }
    
    @Inject(at = @At("HEAD"), method = "onHurt", cancellable = true)
    private void dimhoppertweaks$onHurt(EntityLivingBase entity, DamageSource source, float damage,
            CallbackInfoReturnable<Float> cir) {
        if(DelayedModAccess.isInfernalDistracted(getMobTarget())) cir.setReturnValue(damage);
    }
}