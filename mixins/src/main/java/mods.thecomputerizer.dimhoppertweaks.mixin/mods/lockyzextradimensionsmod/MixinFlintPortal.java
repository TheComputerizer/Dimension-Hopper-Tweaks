package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lockyzextradimensionsmod;

import mod.mcreator.mcreator_dimensionflint.BlockTutorialPortal;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import static com.kashdeya.tinyprogressions.inits.TechBlocks.flint_block;
import static net.minecraft.block.BlockPortal.AXIS;
import static net.minecraft.block.material.Material.PORTAL;
import static net.minecraft.init.Blocks.AIR;
import static net.minecraft.util.EnumFacing.Axis.X;
import static net.minecraft.util.EnumFacing.Axis.Z;

@SuppressWarnings({"NullableProblems", "deprecation"})
@Mixin(value = BlockTutorialPortal.class, remap = false)
public abstract class MixinFlintPortal extends Block {

    public MixinFlintPortal() {
        super(PORTAL);
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
        byte xByte = (byte)(dimhoppertweaks$checkBlock(world,x,y,z,true,flint_block) ? 1 : 0);
        byte zByte = (byte)(dimhoppertweaks$checkBlock(world,x,y,z,false,flint_block) ? 1 : 0);
        if(xByte == zByte) return false;
        if(dimhoppertweaks$getBlock(world,x-xByte,y,z-zByte)==AIR) {
            x-=xByte;
            z-=zByte;
        }
        int hOffset;
        int vOffset;
        for(hOffset=-1;hOffset<=2;hOffset++) {
            for(vOffset=-1;vOffset<=3;vOffset++) {
                if(hOffset!=-1 && hOffset!=2 || vOffset!=-1 && vOffset!=3) {
                    Block block = dimhoppertweaks$getBlock(world,x+xByte*hOffset,y+vOffset,z+zByte*hOffset);
                    if((hOffset==-1 || hOffset==2 || vOffset==-1 || vOffset==3) && block!=flint_block) return false;
                }
            }
        }
        for(hOffset=0;hOffset<2;hOffset++) {
            for(vOffset=0;vOffset<3;vOffset++) {
                IBlockState state = this.getDefaultState().withProperty(AXIS,xByte==0 ? Z : X);
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
        if(dimhoppertweaks$getBlock(world,x-1,y,z)==instance || dimhoppertweaks$getBlock(world,x+1,y,z)==instance) {
            xOffset = 1;
            zOffset = 0;
        }
        int yAdjusted = y;
        while(dimhoppertweaks$getBlock(world,x,yAdjusted-1,z)==instance) yAdjusted--;
        if(dimhoppertweaks$getBlock(world,x,yAdjusted-1,z)!=flint_block) world.setBlockToAir(new BlockPos(x,y,z));
        else {
            int yOffset = 1;
            while(yOffset<4 && dimhoppertweaks$getBlock(world,x,yAdjusted+yOffset,z)==instance) yOffset++;
            if(yOffset==3 && dimhoppertweaks$getBlock(world,x,yAdjusted+yOffset,z)==flint_block) {
                boolean xPortal = dimhoppertweaks$getBlock(world,x-1,y,z)==instance ||
                        dimhoppertweaks$getBlock(world,x+1,y,z)==instance;
                boolean zPortal = dimhoppertweaks$getBlock(world,x,y,z-1)==instance ||
                        dimhoppertweaks$getBlock(world,x,y,z+1)==instance;
                if(xPortal && zPortal) world.setBlockToAir(new BlockPos(x,y,z));
                else if((dimhoppertweaks$getBlock(world,x+xOffset,y,z+zOffset)!=flint_block ||
                        dimhoppertweaks$getBlock(world,x-xOffset,y,z-zOffset)!=instance) &&
                        (dimhoppertweaks$getBlock(world,x-xOffset,y,z-zOffset)!=flint_block ||
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