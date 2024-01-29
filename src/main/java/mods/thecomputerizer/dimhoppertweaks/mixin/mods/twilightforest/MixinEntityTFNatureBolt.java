package mods.thecomputerizer.dimhoppertweaks.mixin.mods.twilightforest;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.entity.EntityTFNatureBolt;

@Mixin(value = EntityTFNatureBolt.class, remap = false)
public abstract class MixinEntityTFNatureBolt {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;"+
            "addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V"), method = "onImpact", remap = true)
    private void dimhoppertweaks$adjustPotionEffect(EntityLivingBase entity, PotionEffect effect) {
        if(DelayedModAccess.hasGameStage(entity,"hardcore"))
            effect = new PotionEffect(MobEffects.WITHER,effect.getDuration(),effect.getAmplifier());
        entity.addPotionEffect(effect);
    }
}