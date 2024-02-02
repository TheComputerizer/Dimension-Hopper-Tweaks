package mods.thecomputerizer.dimhoppertweaks.mixin.mods.xreliquary;

import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xreliquary.items.ItemMobCharm;

@Mixin(value = ItemMobCharm.class, remap = false)
public abstract class MixinItemMobCharm {

    @Inject(at = @At("HEAD"), method = "damagePlayersMobCharm", cancellable = true)
    private void dimhoppertweaks$noCharmDamageForGCPlayers(EntityPlayer player, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(player instanceof GCEntityClientPlayerMP) cir.setReturnValue(true);
    }
}