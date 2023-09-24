package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import com.kashdeya.tinyprogressions.inits.TechBlocks;
import mod.mcreator.mcreator_dimensionflint;
import mod.mcreator.mcreator_flintOre;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.BlockFlintOreAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("unused")
@Mixin(value = mcreator_dimensionflint.ChunkProviderModded.class, remap = false)
public class MixinFlintChunkProvider {

    @Final @Shadow protected final static IBlockState NETHERRACK = TechBlocks.flint_block.getDefaultState();
    @Final @Shadow protected final static IBlockState GRAVEL = TechBlocks.flint_block.getDefaultState();
    @Final @Shadow protected final static IBlockState SOUL_SAND = TechBlocks.flint_block.getDefaultState();

    @Final @Shadow private final WorldGenerator quartzGen = new WorldGenMinable(((BlockFlintOreAccess)mcreator_flintOre.block).dimhoppertweaks$getDefaultState(), 14, BlockMatcher.forBlock(TechBlocks.flint_block));
    @Final @Shadow private final WorldGenerator magmaGen = new WorldGenMinable(TechBlocks.flint_block.getDefaultState(), 14, BlockMatcher.forBlock(TechBlocks.flint_block));
}
