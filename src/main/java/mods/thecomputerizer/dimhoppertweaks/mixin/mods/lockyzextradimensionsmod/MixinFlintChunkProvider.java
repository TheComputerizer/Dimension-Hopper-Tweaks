package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lockyzextradimensionsmod;

import mod.mcreator.mcreator_dimensionflint;
import mod.mcreator.mcreator_flintOre;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IBlockFlintOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static com.kashdeya.tinyprogressions.inits.TechBlocks.flint_block;

@SuppressWarnings("unused")
@Mixin(value = mcreator_dimensionflint.ChunkProviderModded.class, remap = false)
public abstract class MixinFlintChunkProvider {

    @Final @Shadow protected final static IBlockState NETHERRACK = flint_block.getDefaultState();
    @Final @Shadow protected final static IBlockState GRAVEL = flint_block.getDefaultState();
    @Final @Shadow protected final static IBlockState SOUL_SAND = flint_block.getDefaultState();
    @Final @Shadow private final WorldGenerator quartzGen = new WorldGenMinable(((IBlockFlintOre)mcreator_flintOre.block)
            .dimhoppertweaks$getDefaultState(),14,BlockMatcher.forBlock(flint_block));
    @Final @Shadow private final WorldGenerator magmaGen = new WorldGenMinable(flint_block.getDefaultState(),
                                14,BlockMatcher.forBlock(flint_block));
}