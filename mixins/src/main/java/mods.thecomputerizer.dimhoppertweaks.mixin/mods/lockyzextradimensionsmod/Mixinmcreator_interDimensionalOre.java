package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lockyzextradimensionsmod;

import mod.mcreator.mcreator_interDimensionalOre;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(value = mcreator_interDimensionalOre.class, remap = false)
public abstract class Mixinmcreator_interDimensionalOre {

    /**
     * @author The_Computerizer
     * @reason Remove interdimensional ore generation
     */
    @Overwrite
    public void generateSurface(World world, Random rand, int chunkX, int chunkZ) {}
}