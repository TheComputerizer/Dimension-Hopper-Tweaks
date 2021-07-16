package mods.thecomputerizer.dimensionhoppertweaks.generator;

import gcewing.sg.BaseOrientation;
import gcewing.sg.SGCraft;
import gcewing.sg.block.SGRingBlock;
import mods.thecomputerizer.dimensionhoppertweaks.util.Lazy;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static gcewing.sg.SGCraft.sgBaseBlock;

public final class Stargate extends DimHopperStruct {
    // Lazy to ensure initialization order doesn't fuck us up
    public static final Lazy<IBlockState> BASE = Lazy.of(() -> sgBaseBlock.getDefaultState().withProperty(BaseOrientation.Orient4WaysByState.FACING, EnumFacing.NORTH));
    public static final Lazy<IBlockState> RING_1 = Lazy.of(() -> SGCraft.sgRingBlock.getDefaultState().withProperty(SGRingBlock.VARIANT, 0));
    public static final Lazy<IBlockState> RING_2 = Lazy.of(() -> SGCraft.sgRingBlock.getDefaultState().withProperty(SGRingBlock.VARIANT, 1));
    public static final Lazy<IBlockState> A = Lazy.of(Blocks.AIR::getDefaultState);

    public Stargate() {
        super("stargate");
    }

    @Override
    public void build(World world, BlockPos basePos) {
        final IBlockState base = BASE.get();
        final IBlockState ring1 = RING_1.get();
        final IBlockState ring2 = RING_2.get();
        final IBlockState a = A.get();
        addBlock(world, basePos, 0, 0, 0, base);
        addBlock(world, basePos, 1, 0, 0, ring1);
        addBlock(world, basePos, -1, 0, 0, ring1);
        addBlock(world, basePos, 2, 0, 0, ring2);
        addBlock(world, basePos, -2, 0, 0, ring2);
        addBlock(world, basePos, 2, 1, 0, ring1);
        addBlock(world, basePos, -2, 1, 0, ring1);
        addBlock(world, basePos, 2, 2, 0, ring2);
        addBlock(world, basePos, -2, 2, 0, ring2);
        addBlock(world, basePos, 1, 2, 0, a);
        addBlock(world, basePos, -1, 2, 0, a);
        addBlock(world, basePos, 2, 3, 0, ring1);
        addBlock(world, basePos, -2, 3, 0, ring1);
        addBlock(world, basePos, 1, 3, 0, a);
        addBlock(world, basePos, -1, 3, 0, a);
        addBlock(world, basePos, 2, 4, 0, ring2);
        addBlock(world, basePos, -2, 4, 0, ring2);
        addBlock(world, basePos, 1, 4, 0, ring1);
        addBlock(world, basePos, -1, 4, 0, ring1);
        addBlock(world, basePos, 0, 4, 0, ring2);
    }
}
