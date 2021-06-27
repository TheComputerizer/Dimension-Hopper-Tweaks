package mods.thecomputerizer.dimensionhoppertweaks.generator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public abstract class DimHopperStruct extends WorldGenerator {
    private final String name;

    public DimHopperStruct(String name) {
        this.name = name;
        handler.registerStructure(this);
    }

    private DimHopperStruct() {
        this.name = "EMPTY";
    }

    public final String getName() {
        return name;
    }

    @Override
    public final boolean generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos position) {
        build(world, rand, new BlockPos.MutableBlockPos(position));
        return true;
    }

    protected abstract void build(World world, Random rand, BlockPos basePos);

    public final void addBlock(World world, BlockPos pos, int xCoordOffset, int yCoordOffset, int zCoordOffset, IBlockState block) {
        world.setBlockState(pos.add(xCoordOffset, yCoordOffset, zCoordOffset), block, 2);
    }

    static final class EmptyStructure extends DimHopperStruct {
        EmptyStructure() {
            super();
        }

        @Override
        protected void build(World world, Random rand, BlockPos basePos) {}
    }
}
