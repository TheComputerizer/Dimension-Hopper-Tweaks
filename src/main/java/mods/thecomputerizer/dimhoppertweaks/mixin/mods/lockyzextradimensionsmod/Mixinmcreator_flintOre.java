package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lockyzextradimensionsmod;

import mod.mcreator.mcreator_flintOre;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(value = mcreator_flintOre.class, remap = false)
public abstract class Mixinmcreator_flintOre {

    /**
     * @author The_Computerizer
     * @reason Remove flint ore generation
     */
    @Overwrite
    public void generateSurface(World world, Random rand, int chunkX, int chunkZ) {}
}