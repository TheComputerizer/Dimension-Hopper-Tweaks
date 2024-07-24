package mods.thecomputerizer.dimhoppertweaks.mixin.mods.infernalmobs;

import atomicstryker.infernalmobs.common.MobModifier;
import atomicstryker.infernalmobs.common.mods.MM_Berserk;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MM_Berserk.class, remap = false)
public abstract class MixinMM_Berserk extends MobModifier {
    
    @Inject(at = @At("HEAD"), method = "onAttack", cancellable = true)
    private void dimhoppertweaks$onAttack(EntityLivingBase entity, DamageSource source, float damage,
            CallbackInfoReturnable<Float> cir) {
        if(DelayedModAccess.isInfernalDistracted(entity)) cir.setReturnValue(damage);
    }
}