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
        int dist;
        for(dist=0; dist<22; dist++) {
            BlockPos blockpos = pos.offset(facing,dist);
            if(!this.isEmptyBlock(this.world.getBlockState(blockpos).getBlock()) ||
                    !dimhoppertweaks$isCompressedObsidian(this.world.getBlockState(blockpos.down()).getBlock()))
                break;
        }
        return dimhoppertweaks$isCompressedObsidian(this.world.getBlockState(pos.offset(facing,dist)).getBlock()) ? dist : 0;
    }

    /**
     * @author The_Computerizer
     * @reason Custom Nether portal implementation
     */
    @Overwrite
    protected int calculatePortalHeight() {
        portalValidity: {
            for(this.height=0; this.height<21; this.height++) {
                for(int w=0; w<this.width; w++) {
                    BlockPos blockpos = this.bottomLeft.offset(this.rightDir,w).up(this.height);
                    Block block = this.world.getBlockState(blockpos).getBlock();
                    if(!this.isEmptyBlock(block)) break portalValidity;
                    if(block==Blocks.PORTAL) this.portalBlockCount++;
                    if(w==0) {
                        block = this.world.getBlockState(blockpos.offset(this.leftDir)).getBlock();
                        if(!dimhoppertweaks$isCompressedObsidian(block)) break portalValidity;
                    } else if(w==this.width-1) {
                        block = this.world.getBlockState(blockpos.offset(this.rightDir)).getBlock();
                        if(!dimhoppertweaks$isCompressedObsidian(block)) break portalValidity;
                    }
                }
            }
        }
        for(int w=0; w<this.width; w++) {
            if(!dimhoppertweaks$isCompressedObsidian(this.world.getBlockState(this.bottomLeft.offset(this.rightDir,w)
                    .up(this.height)).getBlock())) {
                this.height = 0;
                break;
            }
        }
        if(this.height<=21 && this.height>=3) return this.height;
        this.bottomLeft = null;
        this.width = 0;
        this.height = 0;
        return 0;
    }

    @Unique
    private boolean dimhoppertweaks$isCompressedObsidian(Block block) {
        return Objects.nonNull(block.getRegistryName()) && block.getRegistryName().toString().equals("overloaded:compressed_obsidian");
    }
}