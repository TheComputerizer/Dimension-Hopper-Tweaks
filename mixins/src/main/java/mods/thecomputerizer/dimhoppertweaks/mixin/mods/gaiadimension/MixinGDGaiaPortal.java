package mods.thecomputerizer.dimhoppertweaks.mixin.mods.gaiadimension;

import androsa.gaiadimension.block.GDGaiaPortal;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;
import java.util.Objects;

import static androsa.gaiadimension.registry.GDBlocks.gaia_portal;

@Mixin(value = GDGaiaPortal.class, remap = false)
public abstract class MixinGDGaiaPortal {

    /**
     * @author The_Computerizer
     * @reason Switch to vanilla like implementation so the portal position is calculated correctly
     */
    @Overwrite(remap = true)
    public void onEntityCollision(World world, @Nullable BlockPos pos, IBlockState state, Entity entity) {
        if(!entity.isRiding() && !entity.isBeingRidden() && entity.isNonBoss())
            ((IEntity)entity).dimhoppertweaks$setPortalOther(gaia_portal, Objects.nonNull(pos) ? pos : entity.getPosition());
    }
}