package mods.thecomputerizer.dimhoppertweaks.registry.structures;

import gcewing.sg.BaseOrientation;
import gcewing.sg.SGCraft;
import gcewing.sg.block.SGRingBlock;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.util.Lazy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class StargateStructure extends AbstractStructure {

    @SuppressWarnings("unchecked")
    public static final Lazy<IBlockState> BASE = Lazy.of(() -> SGCraft.sgBaseBlock.getDefaultState()
            .withProperty(BaseOrientation.Orient4WaysByState.FACING,EnumFacing.NORTH));
    public static final Lazy<IBlockState> RING_1 = Lazy.of(() -> SGCraft.sgRingBlock.getDefaultState()
            .withProperty(SGRingBlock.VARIANT, 0));
    public static final Lazy<IBlockState> RING_2 = Lazy.of(() -> SGCraft.sgRingBlock.getDefaultState()
            .withProperty(SGRingBlock.VARIANT, 1));
    public static final Lazy<IBlockState> A = Lazy.of(Blocks.AIR::getDefaultState);

    public StargateStructure() {
        setRegistryName(Constants.res("stargate"));
    }

    @Override
    public boolean build(World world, BlockPos pos) {
        final IBlockState base = BASE.get();
        final IBlockState ring1 = RING_1.get();
        final IBlockState ring2 = RING_2.get();
        final IBlockState a = A.get();
        addBlock(world,pos,0,0,0,base);
        addBlock(world,pos,1,0,0,ring1);
        addBlock(world,pos,-1,0,0,ring1);
        addBlock(world,pos,2,0,0,ring2);
        addBlock(world,pos,-2,0,0,ring2);
        addBlock(world,pos,2,1,0,ring1);
        addBlock(world,pos,-2,1,0,ring1);
        addBlock(world,pos,2,2,0,ring2);
        addBlock(world,pos,-2,2,0,ring2);
        addBlock(world,pos,1,2,0,a);
        addBlock(world,pos,-1,2,0,a);
        addBlock(world,pos,2,3,0,ring1);
        addBlock(world,pos,-2,3,0,ring1);
        addBlock(world,pos,1,3,0,a);
        addBlock(world,pos,-1,3,0,a);
        addBlock(world,pos,2,4,0,ring2);
        addBlock(world,pos,-2,4,0,ring2);
        addBlock(world,pos,1,4,0,ring1);
        addBlock(world,pos,-1,4,0,ring1);
        addBlock(world,pos,0,4,0,ring2);
        return true;
    }
}
