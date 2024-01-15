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

import java.util.Objects;

@Mixin(value = PieceTrickCollapseBlock.class, remap = false)
public abstract class MixinPieceTrickCollapseBlock {

    @Shadow SpellParam position;

    /**
     * @author The_Computerizer
     * @reason Account for ore stages so that staged blocks get turned into the falling block version of the replacement instead of the original
     */
    @Overwrite
    public Object execute(SpellContext ctx) throws SpellRuntimeException {
        ItemStack tool = ctx.getHarvestTool();
        Vector3 posVec = ((SpellPiece)(Object)this).getParamValue(ctx,this.position);
        if(Objects.isNull(posVec)) throw new SpellRuntimeException("psi.spellerror.nullvector");
        else if(!ctx.isInRadius(posVec)) throw new SpellRuntimeException("psi.spellerror.outsideradius");
        else {
            World world = ctx.caster.getEntityWorld();
            BlockPos pos = posVec.toBlockPos();
            BlockPos posDown = pos.down();
            IBlockState state = world.getBlockState(pos);
            IBlockState stateDown = world.getBlockState(posDown);
            Block block = state.getBlock();
            Block blockBelow = stateDown.getBlock();
            if(world.isBlockModifiable(ctx.caster,pos)) {
                if(blockBelow.isAir(stateDown,world,posDown) && state.getBlockHardness(world,pos)!=-1f &&
                        PieceTrickBreakBlock.canHarvestBlock(block,ctx.caster,world,pos,tool) &&
                        Objects.isNull(world.getTileEntity(pos)) && block.canSilkHarvest(world,pos,state,ctx.caster)) {
                    BlockEvent.BreakEvent event = PieceTrickBreakBlock.createBreakEvent(state,ctx.caster,world,pos,tool);
                    MinecraftForge.EVENT_BUS.post(event);
                    if(event.isCanceled()) return null;
                    if(state.getBlock()==Blocks.LIT_REDSTONE_ORE) {
                        state = Blocks.REDSTONE_ORE.getDefaultState();
                        world.setBlockState(pos,state);
                    }
                    EntityFallingBlock falling = new EntityFallingBlock(world,(double)pos.getX()+0.5d,
                            (double)pos.getY()+0.5d,(double)pos.getZ()+0.5d,
                            Loader.isModLoaded("orestages") ?
                                    PsiUtil.accountForOreStages(event.getPlayer(),state) : state);
                    world.spawnEntity(falling);
                }
            }
            return null;
        }
    }
}