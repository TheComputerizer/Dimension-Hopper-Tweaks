package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lockyzextradimensionsmod;

import mod.mcreator.mcreator_alternateOre;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(value = mcreator_alternateOre.class, remap = false)
public abstract class Mixinmcreator_alternateOre {

    /**
     * @author The_Computerizer
     * @reason Remove alternate ore generation
     */
    @Overwrite
    public void generateNether(World world, Random rand, int chunkX, int chunkZ) {}
}