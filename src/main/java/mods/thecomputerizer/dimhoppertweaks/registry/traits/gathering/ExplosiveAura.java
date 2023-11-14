package mods.thecomputerizer.dimhoppertweaks.registry.traits.gathering;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class ExplosiveAura extends ExtendedEventsTrait {

    public ExplosiveAura() {
        super("explosive_aura",1,3,GATHERING,8,"gathering|16");
    }

    @Override
    public void onExplosionDetonate(ExplosionEvent.Detonate ev) {
        for(BlockPos pos : ev.getAffectedBlocks()) {
            Explosion ex = ev.getExplosion();
            World world = ev.getWorld();
            IBlockState state = DelayedModAccess.getWithOreStage(ex.getExplosivePlacedBy(),world.getBlockState(pos));
            if(state.getMaterial()!=Material.AIR) {
                Block block = state.getBlock();
                if(block.canDropFromExplosion(ex))
                    block.dropBlockAsItemWithChance(world,pos,state,1f,0);
                block.onBlockExploded(world,pos,ex);
            }
        }
    }
}
