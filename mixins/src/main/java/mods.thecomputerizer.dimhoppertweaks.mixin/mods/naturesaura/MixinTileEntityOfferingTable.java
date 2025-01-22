package mods.thecomputerizer.dimhoppertweaks.mixin.mods.naturesaura;

import de.ellpeck.naturesaura.blocks.tiles.TileEntityOfferingTable;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileEntityOfferingTable.class, remap = false)
public abstract class MixinTileEntityOfferingTable {

    @Unique
    private TileEntityOfferingTable dimhoppertweaks$cast() {
        return (TileEntityOfferingTable)(Object)this;
    }

    @ModifyConstant(constant = @Constant(longValue = 20L), method = "update", remap = true)
    private long dimhoppertweaks$doubleTime(long original) {
        return DelayedModAccess.isInFastChunk(dimhoppertweaks$cast()) ? original/2L : original;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;"+
            "spawnEntity(Lnet/minecraft/entity/Entity;)Z"), method = "update")
    private boolean dimhoppertweaks$adjustItemGravity(World world, Entity entity) {
        if(DelayedModAccess.isInFastChunk(dimhoppertweaks$cast())) ((IEntity)entity).dimhoppertweaks$setGravityFactor(2d);
        return world.spawnEntity(entity);
    }
}