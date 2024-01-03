package mods.thecomputerizer.dimhoppertweaks.mixin.mods.psi;

import mods.thecomputerizer.dimhoppertweaks.util.PsiUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.common.spell.trick.block.PieceTrickBreakBlock;
import vazkii.psi.common.spell.trick.block.PieceTrickCollapseBlock;

@Mixin(value = PieceTrickCollapseBlock.class, remap = false)
public abstract class MixinPieceTrickCollapseBlock {

    @Shadow SpellParam position;

    /**
     * @author The_Computerizer
     * @reason Account for ore stages so that staged blocks get turned into the falling block version of the replacement instead of the original
     */
    @Overwrite
    public Object execute(SpellContext context) throws SpellRuntimeException {
        ItemStack tool = context.getHarvestTool();
        Vector3 positionVal = ((SpellPiece)(Object)this).getParamValue(context, this.position);
        if (positionVal == null) {
            throw new SpellRuntimeException("psi.spellerror.nullvector");
        } else if (!context.isInRadius(positionVal)) {
            throw new SpellRuntimeException("psi.spellerror.outsideradius");
        } else {
            World world = context.caster.getEntityWorld();
            BlockPos pos = positionVal.toBlockPos();
            BlockPos posDown = pos.down();
            IBlockState state = world.getBlockState(pos);
            IBlockState stateDown = world.getBlockState(posDown);
            Block block = state.getBlock();
            Block blockBelow = stateDown.getBlock();
            if (world.isBlockModifiable(context.caster, pos)) {
                if (blockBelow.isAir(stateDown, world, posDown) && state.getBlockHardness(world, pos) != -1.0F &&
                        PieceTrickBreakBlock.canHarvestBlock(block, context.caster, world, pos, tool) &&
                        world.getTileEntity(pos) == null && block.canSilkHarvest(world, pos, state, context.caster)) {
                    BlockEvent.BreakEvent event = PieceTrickBreakBlock.createBreakEvent(state, context.caster, world, pos, tool);
                    MinecraftForge.EVENT_BUS.post(event);
                    if (event.isCanceled()) return null;
                    if (state.getBlock() == Blocks.LIT_REDSTONE_ORE) {
                        state = Blocks.REDSTONE_ORE.getDefaultState();
                        world.setBlockState(pos, state);
                    }
                    EntityFallingBlock falling = new EntityFallingBlock(world, (double) pos.getX() + 0.5,
                            (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, Loader.isModLoaded("orestages") ?
                            PsiUtil.accountForOreStages(event.getPlayer(), state) : state);
                    world.spawnEntity(falling);
                }
            }
            return null;
        }
    }
}