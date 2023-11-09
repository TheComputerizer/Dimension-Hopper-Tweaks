package mods.thecomputerizer.dimhoppertweaks.common.skills.gathering;

import mods.thecomputerizer.dimhoppertweaks.common.skills.ExtendedEventsTrait;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.ExplosionEvent;

public class ExplosiveAura extends ExtendedEventsTrait {

    public ExplosiveAura() {
        super("explosive_aura",1,3,GATHERING,8,"gathering|16");
    }

    @Override
    public void onExplosionDetonate(ExplosionEvent.Detonate ev) {
        for(BlockPos pos : ev.getAffectedBlocks()) {
            IBlockState state = ev.getWorld().getBlockState(pos);
            if(state.getMaterial()!=Material.AIR) {
                Block block = state.getBlock();
                if(block.canDropFromExplosion(ev.getExplosion()))
                    block.dropBlockAsItemWithChance(ev.getWorld(),pos,state,1f,0);
                block.onBlockExploded(ev.getWorld(),pos,ev.getExplosion());
            }
        }
    }
}
