package mods.thecomputerizer.dimhoppertweaks.mixin.mods.mekanism;

import mekanism.common.tile.component.TileComponentSecurity;
import mekanism.common.tile.prefab.TileEntityContainerBlock;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.ITileEntity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.UUID;

@Mixin(value = TileComponentSecurity.class, remap = false)
public abstract class MixinTileComponentSecurity {

    @Shadow public TileEntityContainerBlock tileEntity;

    @Inject(at = @At("RETURN"), method = "setOwnerUUID")
    private void dimhoppertweaks$afterSetOwnerUUID(UUID uuid, CallbackInfo ci) {
        if(this.tileEntity instanceof ITileEntity && Objects.nonNull(uuid)) {
            EntityPlayer player = this.tileEntity.getWorld().getPlayerEntityByUUID(uuid);
            if(Objects.nonNull(player))
                ((ITileEntity) this.tileEntity).dimhoppertweaks$setStages(DelayedModAccess.getPlayerStages(player));
        }
    }
}