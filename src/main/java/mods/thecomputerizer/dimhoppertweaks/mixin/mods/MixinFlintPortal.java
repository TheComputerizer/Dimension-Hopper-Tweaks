package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import com.kashdeya.tinyprogressions.inits.TechBlocks;
import mod.mcreator.mcreator_dimensionflint;
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

import static mod.mcreator.mcreator_dimensionflint.BlockTutorialPortal.AXIS;

@SuppressWarnings("NullableProblems")
@Mixin(value = mcreator_dimensionflint.BlockTutorialPortal.class, remap = false)
public class MixinFlintPortal extends Block {

    public MixinFlintPortal() {
        super(Material.PORTAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Z));
        this.setTickRandomly(true);
        this.setHardness(-1.0F);
        this.setLightLevel(0.75F);
    }

    /**
     * @author The_Computerizer
     * @reason Cringe MCreator code
     */
    @Overwrite
    public boolean tryToCreatePortal(World world, int x, int y, int z) {
        byte xByte = dimhoppertweaks$checkBlock(world, x, y, z, true, TechBlocks.flint_block) ? (byte) 1 : (byte) 0;
        byte zByte = dimhoppertweaks$checkBlock(world, x, y, z, false, TechBlocks.flint_block) ? (byte) 1 : (byte) 0;
        if (xByte == zByte) return false;
        if (dimhoppertweaks$getBlock(world, x - xByte, y, z - zByte) == Blocks.AIR) {
            x -= xByte;
            z -= zByte;
        }
        int l;
        int i1;
        for (l = -1; l <= 2; ++l) {
            for (i1 = -1; i1 <= 3; ++i1) {
                if (l != -1 && l != 2 || i1 != -1 && i1 != 3) {
                    Block j1 = dimhoppertweaks$getBlock(world, x + xByte * l, y + i1, z + zByte * l);
                    if ((l == -1 || l == 2 || i1 == -1 || i1 == 3) && j1 != TechBlocks.flint_block)
                        return false;
                }
            }
        }

        for (l = 0; l < 2; ++l) {
            for (i1 = 0; i1 < 3; ++i1) {
                IBlockState iblockstate = this.getDefaultState()
                        .withProperty(BlockPortal.AXIS, xByte == 0 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
                world.setBlockState(new BlockPos(x + xByte * l, y + i1, z + zByte * l), iblockstate, 3);
            }
        }
        return true;
    }

    /**
     * More cringe MCreator code
     */
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighbor, BlockPos fromPos) {
        int par2 = pos.getX();
        int par3 = pos.getY();
        int par4 = pos.getZ();
        byte b0 = 0;
        byte b1 = 1;
        if (dimhoppertweaks$getBlock(world, par2 - 1, par3, par4) == this || dimhoppertweaks$getBlock(world, par2 + 1, par3, par4) == this) {
            b0 = 1;
            b1 = 0;
        }

        int i1;
        for(i1 = par3; dimhoppertweaks$getBlock(world, par2, i1 - 1, par4) == this; --i1) {
        }

        if (dimhoppertweaks$getBlock(world, par2, i1 - 1, par4) != TechBlocks.flint_block) {
            world.setBlockToAir(new BlockPos(par2, par3, par4));
        } else {
            int j1;
            for(j1 = 1; j1 < 4 && dimhoppertweaks$getBlock(world, par2, i1 + j1, par4) == this; ++j1) {
            }

            if (j1 == 3 && dimhoppertweaks$getBlock(world, par2, i1 + j1, par4) == TechBlocks.flint_block) {
                boolean flag = dimhoppertweaks$getBlock(world, par2 - 1, par3, par4) == this ||
                        dimhoppertweaks$getBlock(world, par2 + 1, par3, par4) == this;
                boolean flag1 = dimhoppertweaks$getBlock(world, par2, par3, par4 - 1) == this ||
                        dimhoppertweaks$getBlock(world, par2, par3, par4 + 1) == this;
                if (flag && flag1) {
                    world.setBlockToAir(new BlockPos(par2, par3, par4));
                } else if ((dimhoppertweaks$getBlock(world, par2 + b0, par3, par4 + b1) != TechBlocks.flint_block ||
                        dimhoppertweaks$getBlock(world, par2 - b0, par3, par4 - b1) != this) &&
                        (dimhoppertweaks$getBlock(world, par2 - b0, par3, par4 - b1) != TechBlocks.flint_block ||
                                dimhoppertweaks$getBlock(world, par2 + b0, par3, par4 + b1) != this)) {
                    world.setBlockToAir(new BlockPos(par2, par3, par4));
                }
            } else {
                world.setBlockToAir(new BlockPos(par2, par3, par4));
            }
        }

    }

    @Unique private Block dimhoppertweaks$getBlock(World world, int x, int y, int z) {
        return world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    @Unique private boolean dimhoppertweaks$checkBlock(World world, int x, int y, int z, boolean xChange, Block block) {
        return xChange ? dimhoppertweaks$getBlock(world,x-1,y,z)==block || dimhoppertweaks$getBlock(world,x+1,y,z)==block :
                dimhoppertweaks$getBlock(world,x,y,z-1)==block || dimhoppertweaks$getBlock(world,x,y,z+1)==block;
    }
}
