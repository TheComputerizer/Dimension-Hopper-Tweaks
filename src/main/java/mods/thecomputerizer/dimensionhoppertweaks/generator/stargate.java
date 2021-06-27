package mods.thecomputerizer.dimensionhoppertweaks.generator;


import static gcewing.sg.SGCraft.sgBaseBlock;
import static gcewing.sg.SGCraft.sgRingBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class stargate extends DimHopperStruct {
    public static final IBlockState base = sgBaseBlock.getDefaultState();
    public static final IBlockState ring1 = sgRingBlock.getStateFromMeta(0);
    public static final IBlockState ring2 = sgRingBlock.getStateFromMeta(1);

    public stargate() {
        super("stargate");
    }

    @Override
    protected void build(World world, Random rand, BlockPos basePos) {
        addBlock(world, basePos, 0, 0, 0, base);
        addBlock(world, basePos, 1, 0, 0, ring1);
        addBlock(world, basePos, -1, 0, 0, ring1);
        addBlock(world, basePos, 2, 0, 0, ring2);
        addBlock(world, basePos, -2, 0, 0, ring2);
        addBlock(world, basePos, 2, 1, 0, ring1);
        addBlock(world, basePos, -2, 1, 0, ring1);
        addBlock(world, basePos, 2, 2, 0, ring2);
        addBlock(world, basePos, -2, 2, 0, ring2);
        addBlock(world, basePos, 2, 3, 0, ring1);
        addBlock(world, basePos, -2, 3, 0, ring1);
        addBlock(world, basePos, 2, 4, 0, ring2);
        addBlock(world, basePos, -2, 4, 0, ring2);
        addBlock(world, basePos, 1, 4, 0, ring1);
        addBlock(world, basePos, -1, 4, 0, ring1);
        addBlock(world, basePos, 0, 4, 0, ring2);
    }
}
