package mods.thecomputerizer.dimhoppertweaks.mixin.mods.xreliquary;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xreliquary.items.ItemMobCharm;

@Mixin(value = ItemMobCharm.class, remap = false)
public abstract class MixinItemMobCharm {

    @Inject(at = @At("HEAD"), method = "damagePlayersMobCharm", cancellable = true)
    private void dimhoppertweaks$onlyCharmDamageEntityPlayerMP(EntityPlayer player, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(!(player instanceof EntityPlayerMP)) cir.setReturnValue(false);
    }
}