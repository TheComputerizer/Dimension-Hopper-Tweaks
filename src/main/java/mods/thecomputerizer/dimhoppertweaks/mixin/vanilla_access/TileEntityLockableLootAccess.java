package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla_access;

import net.minecraft.tileentity.TileEntityLockableLoot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TileEntityLockableLoot.class)
public interface TileEntityLockableLootAccess {

    @Accessor long getLootTableSeed();
}