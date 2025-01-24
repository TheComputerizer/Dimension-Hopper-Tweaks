package mods.thecomputerizer.dimhoppertweaks.mixin.mods.aoa3;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import net.tslat.aoa3.entity.base.AoATrader;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;

@Mixin(value = AoATrader.class, remap = false)
public abstract class MixinAoATrader extends EntityCreature {
    
    public MixinAoATrader(World world) {
        super(world);
    }
    
    /**
     * Fix compat with Specified Spawning
     */
    @Override
    public boolean isCreatureType(@Nonnull EnumCreatureType type, boolean forSpawnCount) {
        return super.isCreatureType(type,forSpawnCount);
    }
}