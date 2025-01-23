package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EnchantmentHelper.class, remap = false)
public class MixinEnchantmentHelper {
    
    @Inject(at = @At("RETURN"), method = "getDepthStriderModifier", cancellable = true)
    private static void dimhoppertweaks$getDepthStriderModifier(
            EntityLivingBase entity, CallbackInfoReturnable<Integer> cir) {
        if(SkillWrapper.isGoodSwimmer(entity)) cir.setReturnValue(Math.max(3,cir.getReturnValueI()));
    }
}