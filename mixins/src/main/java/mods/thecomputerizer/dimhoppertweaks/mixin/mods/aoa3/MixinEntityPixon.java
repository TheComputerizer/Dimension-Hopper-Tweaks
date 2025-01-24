package mods.thecomputerizer.dimhoppertweaks.mixin.mods.aoa3;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import net.tslat.aoa3.entity.misc.pixon.EntityPixon;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;

@Mixin(value = EntityPixon.class, remap = false)
public abstract class MixinEntityPixon extends EntityCreature {
    
    public MixinEntityPixon(World world) {
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