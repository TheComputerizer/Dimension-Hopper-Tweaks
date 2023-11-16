package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import morph.avaritia.item.tools.ItemSwordInfinity;
import morph.avaritia.util.DamageSourceInfinitySword;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemSwordInfinity.class, remap = false)
public class MixinInfinitySword {

    @Inject(at = @At(value = "HEAD"), method = "hitEntity", cancellable = true)
    private void dimhoppertweaks$hitEntity(ItemStack stack, EntityLivingBase victim, EntityLivingBase player,
                                                  CallbackInfoReturnable<Boolean> cir) {
        if(victim instanceof EntityFinalBoss) {
            victim.attackEntityFrom(new DamageSourceInfinitySword(player),5f);
            cir.setReturnValue(true);
        }
    }
}
