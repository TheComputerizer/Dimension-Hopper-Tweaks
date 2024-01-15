package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lockyzextradimensionsmod;

import com.kashdeya.tinyprogressions.inits.TechBlocks;
import mod.mcreator.mcreator_dimensionflint.BlockTutorialPortal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings({"NullableProblems", "deprecation"})
@Mixin(value = BlockTutorialPortal.class, remap = false)
public abstract class MixinFlintPortal extends Block {

    public MixinFlintPortal() {
        super(Material.PORTAL);
    }

    @Unique
    private BlockTutorialPortal dimhoppertweaks$cast() {
        return (BlockTutorialPortal)(Object)this;
    }

    /**
     * @author The_Computerizer
     * @reason Cringe MCreator code
     */
    @Overwrite
    public boolean tryToCreatePortal(World world, int x, int y, int z) {
        byte xByte = (byte)(dimhoppertweaks$checkBlock(world,x,y,z,true,TechBlocks.flint_block) ? 1 : 0);
        byte zByte = (byte)(dimhoppertweaks$checkBlock(world,x,y,z,false,TechBlocks.flint_block) ? 1 : 0);
        if(xByte == zByte) return false;
        if(dimhoppertweaks$getBlock(world,x-xByte,y,z-zByte)==Blocks.AIR) {
            x -= xByte;
            z -= zByte;
        }
        int hOffset;
        int vOffset;
        for(hOffset = -1; hOffset<=2; hOffset++) {
            for(vOffset = -1; vOffset<=3; vOffset++) {
                if(hOffset!=-1 && hOffset!=2 || vOffset!=-1 && vOffset!=3) {
                    Block block = dimhoppertweaks$getBlock(world,x+xByte*hOffset,y+vOffset,z+zByte*hOffset);
                    if((hOffset==-1 || hOffset==2 || vOffset==-1 || vOffset==3) && block!=TechBlocks.flint_block)
                        return false;
                }
            }
        }
        for(hOffset = 0; hOffset<2; hOffset++) {
            for(vOffset = 0; vOffset<3; vOffset++) {
                IBlockState state = this.getDefaultState().withProperty(
                        BlockPortal.AXIS, xByte==0 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
                world.setBlockState(new BlockPos(x+xByte*hOffset,y+vOffset,z+zByte*hOffset),state,3);
            }
        }
        return true;
    }

    /**
     * More cringe MCreator code
     */
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighbor, BlockPos fromPos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        byte xOffset = 0;
        byte zOffset = 1;
        BlockTutorialPortal instance = dimhoppertweaks$cast();
        if(dimhoppertweaks$getBlock(world,x-1,y,z)==instance ||
                dimhoppertweaks$getBlock(world,x+1,y,z)==instance) {
            xOffset = 1;
            zOffset = 0;
        }
        int yAdjusted = y;
        while(dimhoppertweaks$getBlock(world,x,yAdjusted-1,z)==instance) yAdjusted--;
        if(dimhoppertweaks$getBlock(world,x,yAdjusted-1,z)!=TechBlocks.flint_block)
            world.setBlockToAir(new BlockPos(x,y,z));
        else {
            int yOffset = 1;
            while(yOffset<4 && dimhoppertweaks$getBlock(world,x,yAdjusted+yOffset,z)==instance) yOffset++;
            if(yOffset==3 && dimhoppertweaks$getBlock(world,x,yAdjusted+yOffset,z)==TechBlocks.flint_block) {
                boolean xPortal = dimhoppertweaks$getBlock(world,x-1,y,z)==instance ||
                        dimhoppertweaks$getBlock(world,x+1,y,z)==instance;
                boolean zPortal = dimhoppertweaks$getBlock(world,x,y,z-1)==instance ||
                        dimhoppertweaks$getBlock(world,x,y,z+1)==instance;
                if(xPortal && zPortal) world.setBlockToAir(new BlockPos(x,y,z));
                else if((dimhoppertweaks$getBlock(world,x+xOffset,y,z+zOffset)!=TechBlocks.flint_block ||
                        dimhoppertweaks$getBlock(world,x-xOffset,y,z-zOffset)!=instance) &&
                        (dimhoppertweaks$getBlock(world,x-xOffset,y,z-zOffset)!=TechBlocks.flint_block ||
                                dimhoppertweaks$getBlock(world,x+xOffset,y,z+zOffset)!=instance)) {
                    world.setBlockToAir(new BlockPos(x,y,z));
                }
            } else world.setBlockToAir(new BlockPos(x,y,z));
        }
    }

    @Unique
    private Block dimhoppertweaks$getBlock(World world, int x, int y, int z) {
        return world.getBlockState(new BlockPos(x,y,z)).getBlock();
    }

    @Unique
    private boolean dimhoppertweaks$checkBlock(World world, int x, int y, int z, boolean xChange, Block block) {
        return xChange ?
                dimhoppertweaks$getBlock(world,x-1,y,z)==block || dimhoppertweaks$getBlock(world,x+1,y,z)==block :
                dimhoppertweaks$getBlock(world,x,y,z-1)==block || dimhoppertweaks$getBlock(world,x,y,z+1)==block;
    }
}