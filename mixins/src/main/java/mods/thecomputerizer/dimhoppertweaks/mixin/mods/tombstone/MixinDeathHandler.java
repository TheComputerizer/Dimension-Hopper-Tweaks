package mods.thecomputerizer.dimhoppertweaks.mixin.mods.tombstone;

import mods.thecomputerizer.dimhoppertweaks.config.DHTConfigHelper;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ovh.corail.tombstone.helper.DeathHandler;

@Mixin(value = DeathHandler.class, remap = false)
public abstract class MixinDeathHandler {

    @Inject(at = @At("HEAD"), method = "addPlayerDead")
    private void dimhoppertweaks$adjustXPLoss(EntityPlayerMP player, DamageSource source, CallbackInfo ci) {
        DHTConfigHelper.adjustXPLossPercentage(DelayedModAccess.hasGameStage(player,"hardcore"));
    }
}