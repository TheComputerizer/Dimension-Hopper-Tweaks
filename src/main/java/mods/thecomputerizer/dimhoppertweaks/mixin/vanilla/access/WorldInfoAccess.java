package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla.access;

import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldInfo.class)
public interface WorldInfoAccess {

    @Accessor int getDimension();
}