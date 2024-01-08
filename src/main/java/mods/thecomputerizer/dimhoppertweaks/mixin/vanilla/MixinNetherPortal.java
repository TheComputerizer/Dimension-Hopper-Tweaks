package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;

import java.util.Objects;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
@Mixin(BlockPortal.Size.class)
public abstract class MixinNetherPortal {

    @Shadow @Final private World world;

    @Shadow @Final private EnumFacing rightDir;

    @Shadow @Final private EnumFacing leftDir;

    @Shadow private int portalBlockCount;

    @Shadow private BlockPos bottomLeft;

    @Shadow private int height;

    @Shadow private int width;

    @Shadow protected abstract boolean isEmptyBlock(Block block);

    /**
     * @author The_Computerizer
     * @reason Custom Nether portal implementation
     */
    @Overwrite
    protected int getDistanceUntilEdge(BlockPos pos, EnumFacing facing) {
        int i;
        for (i = 0; i < 22; ++i) {
            BlockPos blockpos = pos.offset(facing, i);
            if (!this.isEmptyBlock(this.world.getBlockState(blockpos).getBlock()) ||
                    !dimhoppertweaks$isCompressedObsidian(this.world.getBlockState(blockpos.down()).getBlock()))
                break;
        }
        return dimhoppertweaks$isCompressedObsidian(this.world.getBlockState(pos.offset(facing, i)).getBlock()) ? i : 0;
    }

    /**
     * @author The_Computerizer
     * @reason Custom Nether portal implementation
     */
    @Overwrite
    protected int calculatePortalHeight() {
        label56:
        for (this.height = 0; this.height < 21; ++this.height) {
            for (int i = 0; i < this.width; ++i) {
                BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i).up(this.height);
                Block block = this.world.getBlockState(blockpos).getBlock();
                if (!this.isEmptyBlock(block)) break label56;
                if (block == Blocks.PORTAL) ++this.portalBlockCount;
                if (i == 0) {
                    block = this.world.getBlockState(blockpos.offset(this.leftDir)).getBlock();
                    if (!dimhoppertweaks$isCompressedObsidian(block)) break label56;
                }
                else if (i == this.width - 1) {
                    block = this.world.getBlockState(blockpos.offset(this.rightDir)).getBlock();
                    if (!dimhoppertweaks$isCompressedObsidian(block)) break label56;
                }
            }
        }
        for (int j = 0; j < this.width; ++j) {
            if (!dimhoppertweaks$isCompressedObsidian(this.world.getBlockState(this.bottomLeft.offset(this.rightDir, j).up(this.height)).getBlock())) {
                this.height = 0;
                break;
            }
        }
        if (this.height <= 21 && this.height >= 3) return this.height;
        this.bottomLeft = null;
        this.width = 0;
        this.height = 0;
        return 0;
    }

    @Unique private boolean dimhoppertweaks$isCompressedObsidian(Block block) {
        return Objects.nonNull(block.getRegistryName()) && block.getRegistryName().toString().equals("overloaded:compressed_obsidian");
    }
}
