package mods.thecomputerizer.dimhoppertweaks.mixin.mods.netherex;

import logictechcorp.netherex.handler.PlayerHandler;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.common.util.ITeleporter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PlayerHandler.class, remap = false)
public abstract class MixinPlayerHandler {
    
    @Redirect(at=@At(value="INVOKE", target="Lnet/minecraft/server/management/PlayerList;"+
            "transferPlayerToDimension(Lnet/minecraft/entity/player/EntityPlayerMP;"+
            "ILnet/minecraftforge/common/util/ITeleporter;)V"),method="onPlayerRespawn")
    private static void dimhoppertweaks$blockStagedDimensions(PlayerList list, EntityPlayerMP player,
            int dimension, ITeleporter teleporter) {
        if(DelayedModAccess.hasStageForDimension(player,dimension))
            list.transferPlayerToDimension(player,dimension,teleporter);
    }
}